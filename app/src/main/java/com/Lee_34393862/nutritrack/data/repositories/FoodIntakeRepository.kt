package com.Lee_34393862.nutritrack.data.repositories

import android.content.Context
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.Lee_34393862.nutritrack.data.AppDatabase
import com.Lee_34393862.nutritrack.data.dao.FoodIntakeDao
import com.Lee_34393862.nutritrack.data.entities.FoodIntake
import kotlinx.coroutines.flow.Flow

class FoodIntakeRepository {

    val foodIntakeDao: FoodIntakeDao

    constructor(context: Context) {
        foodIntakeDao = AppDatabase.getDatabase(context).foodIntakeDao()
    }

    suspend fun insert(foodIntake: FoodIntake) {
        foodIntakeDao.insert(foodIntake)
    }

    suspend fun update(foodIntake: FoodIntake) {
        foodIntakeDao.update(foodIntake)
    }

    suspend fun delete(foodIntake: FoodIntake) {
        foodIntakeDao.delete(foodIntake)
    }

    fun getFoodIntakeByUserId(userId: String): Flow<FoodIntake?> {
        return foodIntakeDao.getFoodIntakeByUserId(userId)
    }

    suspend fun deleteAllFoodIntake() {
        foodIntakeDao.deleteAllFoodIntake()
    }
}