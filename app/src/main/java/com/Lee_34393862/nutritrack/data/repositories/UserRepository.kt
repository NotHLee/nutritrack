package com.Lee_34393862.nutritrack.data.repositories

import android.content.Context
import com.Lee_34393862.nutritrack.data.AppDatabase
import com.Lee_34393862.nutritrack.data.dao.PatientDao
import com.Lee_34393862.nutritrack.data.entities.Patient
import kotlinx.coroutines.flow.firstOrNull

class UserRepository {

    var patientDao: PatientDao
    var currentUser: Patient? = null

    constructor(context: Context) {
        patientDao = AppDatabase.getDatabase(context = context).patientDao()
    }

    suspend fun authenticate(userId: Int, password: String): Result<String> {

        // query for patient entity with userId and match password
        val patient: Patient? = patientDao.getPatientByUserId(userId = userId).firstOrNull()

        // null means user does not exist
        if (patient == null) {
            return Result.failure(Exception("User id does not exist"))
        }

        if (patient.password != password) {
            return Result.failure(Exception("Password does not match"))
        }

        // cache patient as current user if login successful
        currentUser = patient
        return Result.success("Login successful")
    }

    fun logout() {
        currentUser = null
    }
}