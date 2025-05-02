package com.Lee_34393862.nutritrack.data.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.Lee_34393862.nutritrack.data.dao.PatientDao
import com.Lee_34393862.nutritrack.data.entities.Patient

/**
 * This is database class for patient entities
 */
@Database(entities = [Patient::class], version = 1, exportSchema = false)
abstract class PatientDatabase: RoomDatabase() {

    abstract fun patientDao() : PatientDao

    companion object {

        @Volatile
        private var Instance: PatientDatabase? = null;

        fun getDatabase(context: Context): PatientDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, PatientDatabase::class.java, "patient_database")
                    .build().also { Instance = it }
            }
        }

    }


}