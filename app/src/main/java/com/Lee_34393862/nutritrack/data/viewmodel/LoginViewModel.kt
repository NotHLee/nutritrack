package com.Lee_34393862.nutritrack.data.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    object Idle: LoginScreenState()
    object InitialLoading: LoginScreenState()
    object LoginLoading: LoginScreenState()
    object RegisterLoading: LoginScreenState()
}

class LoginViewModel(context: Context) : ViewModel() {

    private val patientRepository = PatientRepository(context = context)

    // store patients as flow such that it observes the db for changes
    private var _patientIds = MutableStateFlow<List<String>>(emptyList())
    val patientIds: StateFlow<List<String>> get() = _patientIds.asStateFlow()
    private var _isLoadingState = MutableStateFlow<LoginScreenState>(LoginScreenState.Idle)
    val isLoadingState: StateFlow<LoginScreenState> get() = _isLoadingState.asStateFlow()

    // ideally, we should save ui state in LoginScreen, however, due to the fact that bottom sheet
    // recomposes when screen rotates, we need to save the state here to avoid losing the state
    var isBottomSheetExpanded by mutableStateOf<Boolean>(false)
    var registerMode by mutableStateOf<Boolean>(false)

    // login sheet state
    var loginSheetDropdownExpanded by mutableStateOf<Boolean>(false)
    var loginUserId by mutableStateOf<String>("")
    var loginPassword by mutableStateOf<String>("")
    var loginPasswordVisible by mutableStateOf<Boolean>(false)

    // register sheet state
    var registerSheetDropdownExpanded by mutableStateOf<Boolean>(false)
    var registerUserId by mutableStateOf<String>("")
    var registerName by mutableStateOf<String>("")
    var registerPhoneNumber by mutableStateOf<String>("")
    var registerPassword by mutableStateOf<String>("")
    var registerConfirmPassword by mutableStateOf<String>("")
    var registerPasswordVisible by mutableStateOf<Boolean>(false)
    var registerConfirmPasswordVisible by mutableStateOf<Boolean>(false)

    fun resetAllStates() {
        isBottomSheetExpanded = false
        registerMode = false
        loginUserId = ""
        loginPassword = ""
        loginPasswordVisible = false
        loginSheetDropdownExpanded = false
        resetRegisterState()
    }

    fun resetRegisterState() {
        registerUserId = ""
        registerName = ""
        registerPhoneNumber = ""
        registerPassword = ""
        registerConfirmPassword = ""
        registerPasswordVisible = false
        registerConfirmPasswordVisible = false
        registerSheetDropdownExpanded = false
        registerMode = false
    }

    init {
        viewModelScope.launch {
            _isLoadingState.value = LoginScreenState.InitialLoading
            patientRepository.getAllPatientIds().collect { patientIds ->
                _patientIds.value = patientIds
                _isLoadingState.value = LoginScreenState.Idle
            }
        }
    }

    suspend fun login(): Result<String> {

        _isLoadingState.value = LoginScreenState.LoginLoading

        // query for patient entity with userId and match password
        val patientFlow: Flow<Patient?> = patientRepository.getPatientByUserId(userId = loginUserId)
        val patient: Patient? = patientFlow.firstOrNull()

        // null means user does not exist
        if (patient == null) {
            _isLoadingState.value = LoginScreenState.Idle
            return Result.failure(Exception("User id does not exist"))
        }

        // null password means this account has not been claimed, registration is required first
        if (patient.password.isEmpty()) {
            _isLoadingState.value = LoginScreenState.Idle
            return Result.failure(Exception("User id has not been registered"))
        }

        // wrong password
        if (patient.password != loginPassword) {
            _isLoadingState.value = LoginScreenState.Idle
            return Result.failure(Exception("Password does not match"))
        }

        // cache patient as current user for global access
        val updateUserJob = viewModelScope.launch {
            patientRepository.getPatientByUserId(loginUserId).collect { patient ->
                if (patient != null) {
                    AuthManager.saveCurrentUserSession(patient)
                }
            }
        }
        AuthManager.saveUpdateUserJob(updateUserJob)

        _isLoadingState.value = LoginScreenState.Idle
        return Result.success("Login successful")

    }

    suspend fun register(): Result<String> {

        _isLoadingState.value = LoginScreenState.RegisterLoading

        // query for patient entity with userId
        val patientFlow: Flow<Patient?> = patientRepository.getPatientByUserId(userId = registerUserId)
        val patient: Patient? = patientFlow.firstOrNull()

        // null means user does not exist
        if (patient == null) {
            _isLoadingState.value = LoginScreenState.Idle
            return Result.failure(Exception("User id does not exist"))
        }

        // non null password means this account has been claimed
        if (patient.password.isNotEmpty()) {
            _isLoadingState.value = LoginScreenState.Idle
            return Result.failure(Exception("User id has been claimed"))
        }

        // match phone number
        if (patient.phoneNumber != registerPhoneNumber) {
            _isLoadingState.value = LoginScreenState.Idle
            return Result.failure(Exception("Incorrect phone number"))
        }

        // ensure name is not empty
        if (registerName.isEmpty()) {
            _isLoadingState.value = LoginScreenState.Idle
            return Result.failure(Exception("Name cannot be empty"))
        }

        // ensure password is not empty
        if (registerPassword.isEmpty()) {
            _isLoadingState.value = LoginScreenState.Idle
            return Result.failure(Exception("Password cannot be empty"))
        }

        // ensure password and confirm password are the same
        if (registerPassword != registerConfirmPassword) {
            _isLoadingState.value = LoginScreenState.Idle
            return Result.failure(Exception("Passwords do not match"))
        }

        patientRepository.update(patient.copy(name = registerName, password = registerPassword))
        _isLoadingState.value = LoginScreenState.Idle
        return Result.success("User id successfully claimed")
    }

    @Suppress("UNCHECKED_CAST")
    class LoginViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T: ViewModel> create(modelClass: Class<T>): T =
            LoginViewModel(context) as T
    }
}