package com.Lee_34393862.nutritrack.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Lee_34393862.nutritrack.data.entities.Message
import com.Lee_34393862.nutritrack.data.network.FruityViceResponseModel
import com.Lee_34393862.nutritrack.data.network.GenAIService
import com.Lee_34393862.nutritrack.data.repositories.FruitSuggestion
import com.Lee_34393862.nutritrack.data.repositories.FruityViceRepository
import com.Lee_34393862.nutritrack.data.repositories.MessageRepository
import com.Lee_34393862.nutritrack.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class NutritrackViewModel(
    private val userRepository: UserRepository,
    private val fruityViceRepository: FruityViceRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val genAIService = GenAIService()
    val fruitNameSuggestions: StateFlow<List<FruitSuggestion>> = fruityViceRepository.fruitNameSuggestions
    private val _fruitDetails=  MutableStateFlow<FruityViceResponseModel?>(null)
    private val _currentMotivationalMessage = MutableStateFlow<String?>(null)
    private val _motivationalMessages = MutableStateFlow<List<String>>(emptyList())
    val fruitDetails: StateFlow<FruityViceResponseModel?> = _fruitDetails.asStateFlow()
    val currentMotivationalMessage: StateFlow<String?> = _currentMotivationalMessage.asStateFlow()
    val motivationalMessages: StateFlow<List<String>> = _motivationalMessages.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.currentUser.collect { user ->
                if (user != null) {
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
                Log.d("fruit", fruit.toString())
                _fruitDetails.value = fruit
            }
            .onFailure { error ->
                Log.d("error", error.message.toString())
                _fruitDetails.value = null
            }
    }

    fun generateMotivationalMessage() {
        val prompt = "Generate a short encouraging message to help someone improve their fruit intake."
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
}