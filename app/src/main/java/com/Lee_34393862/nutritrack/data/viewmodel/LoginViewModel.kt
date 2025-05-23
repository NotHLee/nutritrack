package com.Lee_34393862.nutritrack.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.Lee_34393862.nutritrack.data.AuthManager
import com.Lee_34393862.nutritrack.data.entities.Patient
import com.Lee_34393862.nutritrack.data.repositories.PatientRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

sealed class LoginScreenState {
    data object Idle: LoginScreenState()
    data object InitialLoading: LoginScreenState()
    data object LoginLoading: LoginScreenState()
    data object RegisterLoading: LoginScreenState()
}

class LoginViewModel(context: Context) : ViewModel() {

    private val patientRepository = PatientRepository(context = context)

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

        _isLoadingState.value = LoginScreenState.LoginLoading

        // query for patient entity with userId and match password
        val patientFlow: Flow<Patient?> = patientRepository.getPatientByUserId(userId = userId)
        val patient: Patient? = patientFlow.firstOrNull()

        // null means user does not exist
        if (patient == null) {
            return Result.failure(Exception("User id does not exist"))
        }

        // null password means this account has not been claimed, registration is required first
        if (patient.password.isEmpty()) {
            return Result.failure(Exception("User id has not been registered"))
        }

        // wrong password
        if (patient.password != password) {
            return Result.failure(Exception("Password does not match"))
        }

        // cache patient as current user for global access
        viewModelScope.launch {
            patientFlow.collect { patient ->
                patient?.let { AuthManager.saveCurrentUserSession(it) }
            }
        }

        _isLoadingState.value = LoginScreenState.Idle
        return Result.success("Login successful")

    }

    suspend fun register(userId: String,
                         name: String,
                         phoneNumber: String,
                         password: String,
                         confirmPassword: String
    ): Result<String> {

        _isLoadingState.value = LoginScreenState.RegisterLoading

        // query for patient entity with userId
        val patientFlow: Flow<Patient?> = patientRepository.getPatientByUserId(userId = userId)
        val patient: Patient? = patientFlow.firstOrNull()

        // null means user does not exist
        if (patient == null) {
            return Result.failure(Exception("User id does not exist"))
        }

        // non null password means this account has been claimed
        if (patient.password.isNotEmpty()) {
            return Result.failure(Exception("User id has been claimed"))
        }

        // match phone number
        if (patient.phoneNumber != phoneNumber) {
            return Result.failure(Exception("Incorrect phone number"))
        }

        // ensure name is not empty
        if (name.isEmpty()) {
            return Result.failure(Exception("Name cannot be empty"))
        }

        // ensure password is not empty
        if (password.isEmpty()) {
            return Result.failure(Exception("Password cannot be empty"))
        }

        // ensure password and confirm password are the same
        if (password != confirmPassword) {
            return Result.failure(Exception("Passwords do not match"))
        }

        patientRepository.update(patient.copy(name = name, password = password))
        _isLoadingState.value = LoginScreenState.Idle
        return Result.success("User id successfully claimed")
    }

    class LoginViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T: ViewModel> create(modelClass: Class<T>): T =
            LoginViewModel(context) as T
    }
}