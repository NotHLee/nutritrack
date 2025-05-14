package com.Lee_34393862.nutritrack.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.Lee_34393862.nutritrack.data.dao.FoodIntakeDao
import com.Lee_34393862.nutritrack.data.dao.PatientDao
import com.Lee_34393862.nutritrack.data.entities.FoodIntake
import com.Lee_34393862.nutritrack.data.entities.Patient

/**
 * This is database class for patient entities
 */
@Database(entities = [Patient::class, FoodIntake::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun patientDao() : PatientDao
    abstract fun foodIntakeDao(): FoodIntakeDao

    companion object {

        @Volatile
        private var Instance: AppDatabase? = null;

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
                    .build().also { Instance = it }
            }
        }

    }


}