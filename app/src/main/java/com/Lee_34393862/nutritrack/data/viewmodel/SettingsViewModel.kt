package com.Lee_34393862.nutritrack.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.Lee_34393862.nutritrack.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(context: Context, private val userRepository: UserRepository) : ViewModel() {

    private var _currentUserName = MutableStateFlow<String>("")
    private var _currentUserPhoneNumber = MutableStateFlow<String>("")
    private var _currentUserId = MutableStateFlow<String>("")
    val currentUserName = _currentUserName.asStateFlow()
    val currentUserPhoneNumber = _currentUserPhoneNumber.asStateFlow()
    val currentUserId = _currentUserId.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.currentUser.collect { user ->
                _currentUserName.value = user?.name ?: ""
                _currentUserPhoneNumber.value = user?.phoneNumber ?: ""
                _currentUserId.value = user?.userId ?: ""
            }
        }
    }

    fun logout() {
        userRepository.logout()
    }

    // function to login into the clinician screen
    fun clinicianLogin(key: String): Result<String> {
        val secretKey = "dollar-entry-apples"
        return if (key == secretKey) {
            Result.success("Clinician login successful")
        } else {
            Result.failure(Exception("Incorrect key"))
        }
    }

    class SettingsViewModelFactory(context: Context, private val userRepository: UserRepository) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(context, userRepository) as T
        }
    }
}