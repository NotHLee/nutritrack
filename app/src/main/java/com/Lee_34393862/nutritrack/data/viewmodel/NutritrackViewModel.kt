package com.Lee_34393862.nutritrack.data.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.Lee_34393862.nutritrack.data.entities.Message
import com.Lee_34393862.nutritrack.data.network.FruityViceResponseModel
import com.Lee_34393862.nutritrack.data.network.GenAIService
import com.Lee_34393862.nutritrack.data.repositories.FruitSuggestion
import com.Lee_34393862.nutritrack.data.repositories.FruityViceRepository
import com.Lee_34393862.nutritrack.data.repositories.MessageRepository
import com.Lee_34393862.nutritrack.data.repositories.User
import com.Lee_34393862.nutritrack.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class NutritrackViewModel(context: Context, private val userRepository: UserRepository) : ViewModel() {

    private val fruityViceRepository = FruityViceRepository()
    private val messageRepository = MessageRepository(context = context)

    private val genAIService = GenAIService()
    private val _currentUser = MutableStateFlow<User?>(null)
    private val _fruitSuggestions = MutableStateFlow<List<FruitSuggestion>>(emptyList())
    private val _fruitDetails = MutableStateFlow<FruityViceResponseModel?>(null)
    private val _currentMotivationalMessage = MutableStateFlow<String?>(null)
    private val _motivationalMessages = MutableStateFlow<List<String>>(emptyList())
    val fruitSuggestion: StateFlow<List<FruitSuggestion>> = _fruitSuggestions.asStateFlow()
    val fruitDetails: StateFlow<FruityViceResponseModel?> = _fruitDetails.asStateFlow()
    val currentMotivationalMessage: StateFlow<String?> = _currentMotivationalMessage.asStateFlow()
    val motivationalMessages: StateFlow<List<String>> = _motivationalMessages.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.currentUser.collect { user ->
                if (user != null) {
                    _currentUser.value = user
                    // fruit score is only optimal if fruit heifa score is the max
                    when (user.fruitHeifaScore >= 10.0) {
                        true -> { }     // nutritrack will load a random image if optimal fruit score
                        false -> {
                            // get fruit name suggestions for the custom fruit search bar
                            fruityViceRepository.getFruitSuggestions()
                                .onSuccess { suggestions ->
                                    _fruitSuggestions.value = suggestions
                                }
                                .onFailure { error ->
                                    throw error
                                }
                        }
                    }
                    // load messages from the db
                    messageRepository.getMessagesByUserId(user.userId).collect { messages ->
                        if (messages != null) {
                            // reversed so it is sorted from latest to oldest
                            _motivationalMessages.value =
                                messages.map { message -> message.response }.reversed()
                        }
                    }
                }
            }
        }
    }

    suspend fun searchFruit(fruitId: Int) {
        fruityViceRepository.getFruit(fruitId)
            .onSuccess { fruit ->
                _fruitDetails.value = fruit
            }
            .onFailure { error ->
                _fruitDetails.value = null
            }
    }

    fun generateMotivationalMessage() {
        val prompt = """
            The user has the following profile:
            ${_currentUser.value.toString()}
            Generate a short, encouraging and personalized message with emojis to help someone improve their fruit intake based on his/her profile.
            Example message:
            Hey! Just a little reminder that adding more fruit to your day is a great way to boost your health and energy. Even a small handful or a slice can make a difference. Keep going, you got this! 🍎 🍓 🍌 
        """.trimIndent()
        val fullResponse = StringBuilder()
        // stream text so that it dynamically generates the output rather than output it in one go
        viewModelScope.launch {
            genAIService.generateContentStream(prompt = prompt)
                .collect { response ->
                    response.text?.let { output ->
                        fullResponse.append(output)
                        _currentMotivationalMessage.value = fullResponse.toString().trimEnd()
                    }
                }
            // make sure the newlines are trimmed
            val finalMessage = fullResponse.toString().trimEnd()

            // then, save message to db
            userRepository.currentUser.first().let { user ->
                when (user) {
                    null -> {}
                    else -> { messageRepository.insert(
                        Message(
                            userId = user.userId,
                            response = finalMessage
                        )
                    ) }
                }
            }
        }
    }

    class NutritrackViewModelFactory(context: Context, private val userRepository: UserRepository) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return NutritrackViewModel(context, userRepository) as T
        }
    }
}