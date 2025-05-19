package com.Lee_34393862.nutritrack.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.Lee_34393862.nutritrack.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(context: Context, private val userRepository: UserRepository): ViewModel() {

    // store current user's name and total food score as flow
    private var _currentUserName = MutableStateFlow<String>("")
    private var _currentUserTotalFoodScore = MutableStateFlow<Double>(0.0)
    val currentUserName: StateFlow<String> get() = _currentUserName.asStateFlow()
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

    class HomeViewModelFactory(context: Context, private val userRepository: UserRepository): ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T: ViewModel> create(modelClass: Class<T>): T =
            HomeViewModel(context, userRepository) as T
    }
}