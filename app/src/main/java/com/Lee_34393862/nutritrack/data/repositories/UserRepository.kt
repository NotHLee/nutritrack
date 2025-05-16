package com.Lee_34393862.nutritrack.data.repositories

import android.content.Context
import com.Lee_34393862.nutritrack.data.AppDatabase
import com.Lee_34393862.nutritrack.data.dao.FoodIntakeDao
import com.Lee_34393862.nutritrack.data.dao.PatientDao
import com.Lee_34393862.nutritrack.data.entities.FoodIntake
import com.Lee_34393862.nutritrack.data.entities.Patient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull

class UserRepository {

    var patientDao: PatientDao
    var foodIntakeDao: FoodIntakeDao

    // hold current userId as state flow
    val currentUserId = MutableStateFlow<Int?>(null)

    constructor(context: Context) {
        patientDao = AppDatabase.getDatabase(context = context).patientDao()
        foodIntakeDao = AppDatabase.getDatabase(context = context).foodIntakeDao()
    }

    suspend fun authenticate(userId: Int, password: String): Result<String> {

        // query for patient entity with userId and match password
        val patient: Patient? = patientDao.getPatientByUserId(userId = userId).firstOrNull()

        // null means user does not exist
        if (patient == null) {
            return Result.failure(Exception("User id does not exist"))
        }

        // null password means this account has not been claimed, registration is required first
        if (patient.password.isEmpty()) {
            return Result.failure(Exception("User id has not been registered"))
        }

        if (patient.password != password) {
            return Result.failure(Exception("Password does not match"))
        }

        return Result.success("Login successful")
    }

    suspend fun register(userId: Int, password: String): Result<String> {

        // query for patient entity with userId and match password
        val patient: Patient? = patientDao.getPatientByUserId(userId = userId).firstOrNull()

        // null means user does not exist
        if (patient == null) {
            return Result.failure(Exception("User id does not exist"))
        }

        // non null password means this account has been claimed
        if (patient.password.isNotEmpty()) {
            return Result.failure(Exception("User id has been claimed"))
        }

        patientDao.update(patient.copy(password = password))
        return Result.success("User id successfully claimed")

    }

    fun logout() {
        currentUserId.value = null
    }
}