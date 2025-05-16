package com.Lee_34393862.nutritrack.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.Lee_34393862.nutritrack.data.entities.Patient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * This interface defines the data access object (DAO) for the patient entity
 */
@Dao
interface PatientDao {
    @Insert
    suspend fun insert(patient: Patient)

    @Update
    suspend fun update(patient: Patient)

    @Delete
    suspend fun delete(patient: Patient)

    @Query("SELECT * FROM patients WHERE userId = :userId LIMIT 1")
    fun getPatientByUserId(userId: Int): Flow<Patient?>

    @Query("DELETE FROM patients")
    suspend fun deleteAllPatients()

    @Query("SELECT * FROM patients ORDER BY userId ASC")
    fun getAllPatients(): StateFlow<List<Patient>?>
}