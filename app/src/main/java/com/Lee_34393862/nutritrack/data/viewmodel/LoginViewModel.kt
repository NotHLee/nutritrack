package com.Lee_34393862.nutritrack.data.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Lee_34393862.nutritrack.data.entities.Patient
import com.Lee_34393862.nutritrack.data.repositories.PatientRepository
import com.Lee_34393862.nutritrack.data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val patientRepository: PatientRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    // store patients as flow such that it observes the db for changes
    private var _patientIds = MutableStateFlow<List<String>>(emptyList())
    val patientIds: StateFlow<List<String>> get() = _patientIds.asStateFlow()

    init {
        viewModelScope.launch {
            patientRepository.getAllPatientIds().collect { patientIds ->
                _patientIds.value = patientIds
            }
        }
    }

    suspend fun login(userId: String, password: String): Result<String> {
        return userRepository.authenticate(userId, password)
    }
}