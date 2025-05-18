package com.Lee_34393862.nutritrack.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Lee_34393862.nutritrack.data.repositories.PatientRepository
import com.Lee_34393862.nutritrack.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginScreenState {
    data object Idle: LoginScreenState()
    data object InitialLoading: LoginScreenState()
    data object LoginLoading: LoginScreenState()
    data object RegisterLoading: LoginScreenState()
}

class LoginViewModel(
    private val patientRepository: PatientRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    // store patients as flow such that it observes the db for changes
    private var _patientIds = MutableStateFlow<List<String>>(emptyList())
    val patientIds: StateFlow<List<String>> get() = _patientIds.asStateFlow()
    private var _isLoadingState = MutableStateFlow<LoginScreenState>(LoginScreenState.Idle)
    val isLoadingState: StateFlow<LoginScreenState> get() = _isLoadingState.asStateFlow()

    init {
        viewModelScope.launch {
            _isLoadingState.value = LoginScreenState.InitialLoading
            patientRepository.getAllPatientIds().collect { patientIds ->
                _patientIds.value = patientIds
                _isLoadingState.value = LoginScreenState.Idle
            }
        }
    }

    suspend fun login(userId: String, password: String): Result<String> {
        try {
            _isLoadingState.value = LoginScreenState.LoginLoading
            return userRepository.authenticate(userId, password)
        } finally {
            _isLoadingState.value = LoginScreenState.Idle
        }
    }

    suspend fun register(userId: String,
                         name: String,
                         phoneNumber: String,
                         password: String,
                         confirmPassword: String
    ): Result<String> {
        try {
            _isLoadingState.value = LoginScreenState.RegisterLoading
            return userRepository.register(userId, name, phoneNumber, password, confirmPassword)
        } finally {
            _isLoadingState.value = LoginScreenState.Idle
        }
    }
}