package com.Lee_34393862.nutritrack.data.repositories

import android.content.Context
import com.Lee_34393862.nutritrack.data.AppDatabase
import com.Lee_34393862.nutritrack.data.dao.PatientDao
import com.Lee_34393862.nutritrack.data.entities.Patient
import kotlinx.coroutines.flow.Flow

class PatientRepository {

    val patientDao: PatientDao

    constructor(context: Context) {
        patientDao = AppDatabase.getDatabase(context).patientDao()
    }

    suspend fun insert(patient: Patient) {
        patientDao.insert(patient)
    }

    suspend fun delete(patient: Patient) {
        patientDao.delete(patient)
    }

    suspend fun deleteAllPatients() {
        patientDao.deleteAllPatients()
    }

    fun getAllPatients(): Flow<List<Patient>> = patientDao.getAllPatients()

    fun getAllPatientIds(): Flow<List<String>> = patientDao.getAllPatientIds()

    fun getMaleHeifaScoreAverage(): Flow<Double> = patientDao.getMaleHeifaScoreAverage()

    fun getFemaleHeifaScoreAverage(): Flow<Double> = patientDao.getFemaleHeifaScoreAverage()

}