package com.Lee_34393862.nutritrack.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.Lee_34393862.nutritrack.data.entities.FoodIntake
import kotlinx.coroutines.flow.Flow

/**
 * This interface defines the data access object (DAO) for the food intake response entity
 */
@Dao
interface FoodIntakeDao {
    @Insert
    suspend fun insert(foodIntake: FoodIntake)

    @Update
    suspend fun update(foodIntake: FoodIntake)

    @Delete
    suspend fun delete(foodIntake: FoodIntake)

    @Query("DELETE FROM foodintake")
    suspend fun deleteAllFoodIntake()

    @Query("SELECT * FROM foodintake ORDER BY userId ASC")
    fun getAllFoodIntake(): Flow<List<FoodIntake>>
}