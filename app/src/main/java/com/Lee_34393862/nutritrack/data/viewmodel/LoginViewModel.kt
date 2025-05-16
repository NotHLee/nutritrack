package com.Lee_34393862.nutritrack.data.viewmodel

import android.content.Context
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

class LoginViewModel(private val patientRepository: PatientRepository, userRepository: UserRepository) : ViewModel() {

    private var _patients = MutableStateFlow<List<Patient>>(emptyList())
    val patients: StateFlow<List<Patient>> get() = _patients.asStateFlow()

    suspend fun loadPatients() {
        viewModelScope.launch {
            var test = patientRepository.getAllPatients()
            _patients = patientRepository.getAllPatients()
        }
    }




}