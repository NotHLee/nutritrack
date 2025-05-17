package com.Lee_34393862.nutritrack.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Lee_34393862.nutritrack.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userRepository: UserRepository
): ViewModel() {

    // store current user's name and total food score as flow
    private var _currentUserName = MutableStateFlow<String>("")
    val currentUserName: StateFlow<String> get() = _currentUserName.asStateFlow()
    private var _currentUserTotalFoodScore = MutableStateFlow<Double>(0.0)
    val currentUserTotalFoodScore: StateFlow<Double> get() = _currentUserTotalFoodScore.asStateFlow()

    // initial loading
    init {
        viewModelScope.launch {
            userRepository.currentUser.collect { user ->
                if (user != null) {
                    _currentUserName.value = user.name
                    _currentUserTotalFoodScore.value = user.heifaTotalScore
                }
            }
        }
    }
}