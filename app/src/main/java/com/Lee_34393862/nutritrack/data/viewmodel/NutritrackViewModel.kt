package com.Lee_34393862.nutritrack.data.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Lee_34393862.nutritrack.data.AuthManager
import com.Lee_34393862.nutritrack.data.User
import com.Lee_34393862.nutritrack.data.entities.Message
import com.Lee_34393862.nutritrack.data.network.FruityViceResponseModel
import com.Lee_34393862.nutritrack.data.network.GenAIService
import com.Lee_34393862.nutritrack.data.repositories.FruitSuggestion
import com.Lee_34393862.nutritrack.data.repositories.FruityViceRepository
import com.Lee_34393862.nutritrack.data.repositories.MessageRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

sealed class LoadingState {
    object LoadingInitial: LoadingState()
    object LoadingFruitSearch: LoadingState()
    object Idle: LoadingState()
}

class NutritrackViewModel(context: Context) : ViewModel() {

    private val fruityViceRepository = FruityViceRepository()
    private val messageRepository = MessageRepository(context = context)

    private val genAIService = GenAIService()
    private val _fruitSuggestions = MutableStateFlow<List<FruitSuggestion>>(emptyList())
    private val _fruitDetails = MutableStateFlow<FruityViceResponseModel?>(null)
    private val _currentMotivationalMessage = MutableStateFlow<String?>(null)
    private val _motivationalMessages = MutableStateFlow<List<String>>(emptyList())
    private val _isFruitScoreOptimal = MutableStateFlow<Boolean?>(null)
    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.LoadingInitial)

    val fruitSuggestion: StateFlow<List<FruitSuggestion>> = _fruitSuggestions.asStateFlow()
    val fruitDetails: StateFlow<FruityViceResponseModel?> = _fruitDetails.asStateFlow()
    val currentMotivationalMessage: StateFlow<String?> = _currentMotivationalMessage.asStateFlow()
    val motivationalMessages: StateFlow<List<String>> = _motivationalMessages.asStateFlow()
    val isFruitScoreOptimal: StateFlow<Boolean?> = _isFruitScoreOptimal.asStateFlow()
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    init {
        // observe user and check whether user's fruit score is optimal
        viewModelScope.launch {
            AuthManager.currentUser.collectLatest { user ->
                _loadingState.value = LoadingState.LoadingInitial
                Log.d("a", user?.fruitHeifaScore.toString())
                user?.let {
                    // not optimal means it is not the max heifa fruit score
                    when (user.fruitHeifaScore >= 10) {
                        true -> {
                            _isFruitScoreOptimal.value = true
                        }
                        false -> {
                            _isFruitScoreOptimal.value = false
                        }
                    }
                    _loadingState.value = LoadingState.Idle
                }
            }
        }

        // observe optimal fruit score to decide whether to get fruit suggestions
        // we should only get fruit scores if the fruit search section is going to render
        viewModelScope.launch {
            _isFruitScoreOptimal.collectLatest { isOptimal ->
                Log.d("b", isOptimal.toString())
                _loadingState.value = LoadingState.LoadingInitial
                isOptimal?.let {
                    when (isOptimal) {
                        true -> emptyList<FruitSuggestion>()
                        false -> {
                            fruityViceRepository.getFruitSuggestions()
                                .onSuccess { suggestions ->
                                    _fruitSuggestions.value = suggestions
                                }
                                .onFailure { error ->
                                    throw error
                                }
                        }
                    }
                }
                _loadingState.value = LoadingState.Idle
            }
        }

        // observe user and only return messages for current user
        viewModelScope.launch {
            AuthManager.currentUser.collectLatest { user ->
                Log.d("c", "message")
                _loadingState.value = LoadingState.LoadingInitial
                user?.let {
                    messageRepository.getMessagesByUserId(user.userId).collect { messages ->
                        when(messages != null) {
                            true -> { _motivationalMessages.value = messages.map { message -> message.response }.reversed() }
                            false -> { _motivationalMessages.value = emptyList() }
                        }
                        _loadingState.value = LoadingState.Idle
                    }
                }
            }
        }

        // observe if user has logged out (user is null), and automatically reset viewmodel
        viewModelScope.launch {
            AuthManager.currentUser.collectLatest { user ->
                if (user == null) {
                    reset()
                }
            }
        }
    }

    fun searchFruit(fruitId: Int) {
        viewModelScope.launch {
            _loadingState.value = LoadingState.LoadingFruitSearch
            fruityViceRepository.getFruit(fruitId)
                .onSuccess { fruit ->
                    _fruitDetails.value = fruit
                }
                .onFailure { error ->
                    _fruitDetails.value = null
                }
            _loadingState.value = LoadingState.Idle
        }
    }

    // functional to generate the motivational message dynamically
    // it works by appending chunks of text to a string builder and updating my string stateflow
    fun generateMotivationalMessage() {
        viewModelScope.launch {
            val prompt = """
                The user has the following profile:
                ${AuthManager.currentUser.first().toString()}
                Generate a short, encouraging and personalized message with emojis to help someone improve their fruit intake based on his/her profile.
                Example message:
                Hey! Just a little reminder that adding more fruit to your day is a great way to boost your health and energy. Even a small handful or a slice can make a difference. Keep going, you got this! 🍎 🍓 🍌 
            """.trimIndent()
            val fullResponse = StringBuilder()

            // stream text so that it dynamically generates the output rather than output it in one go
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
            AuthManager.currentUser.first().let { user ->
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

    fun reset() {
        _fruitSuggestions.value = emptyList()
        _fruitDetails.value = null
        _currentMotivationalMessage.value = null
        _motivationalMessages.value = emptyList()
        _isFruitScoreOptimal.value = null
        _loadingState.value = LoadingState.LoadingInitial
    }

    class NutritrackViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return NutritrackViewModel(context) as T
        }
    }
}