package com.Lee_34393862.nutritrack.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Lee_34393862.nutritrack.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

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


}