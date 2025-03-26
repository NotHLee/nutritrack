package com.Lee_34393862.nutritrack.feature.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.Lee_34393862.nutritrack.core.data.PatientRepository
import com.Lee_34393862.nutritrack.core.model.Patient
import kotlin.math.log

class LoginScreenViewModel(
    val patientRepository: PatientRepository,
    val navController: NavHostController,
) : ViewModel() {

//    init {
//        populateUserIds()
//    }

    // data
    var patient: Patient by mutableStateOf(Patient("", ""))
    var userIds: List<String> by mutableStateOf(listOf())

    // ui states
    var userId: String by mutableStateOf("")
    var phoneNumber: String by mutableStateOf("")
    var dropdownExpanded: Boolean by mutableStateOf(false)
    var loginExpanded: Boolean by mutableStateOf(false)

    fun login() {
        patientRepository.queryPatientData(userId, "PhoneNumber")
            .onSuccess { phoneNum ->
                if (this.phoneNumber == phoneNum) {
                    navController.popBackStack("login", true)
                    navController.navigate("home")
                }

                // save current patient for this session
                patient = Patient(phoneNumber = phoneNumber, userId = userId)

                // reset UI
                userId = ""
                phoneNumber = ""
                dropdownExpanded = false
                loginExpanded = false
            }
            .onFailure {

            }
    }

    private fun populateUserIds() {
        patientRepository.getAllUserId().onSuccess { userIds ->
            this.userIds = userIds.toList().map { id -> id.toString() }
        }
    }
}