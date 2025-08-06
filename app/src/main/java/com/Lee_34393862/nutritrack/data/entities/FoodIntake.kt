package com.Lee_34393862.nutritrack.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Represent the responses in the FoodIntake screen
 */
@Entity(
    foreignKeys = [ForeignKey(entity = Patient::class,
        parentColumns = arrayOf("userId"),
        childColumns = arrayOf("userId"),
        onDelete = ForeignKey.CASCADE
    )],
    tableName = "foodintake")
data class FoodIntake (
    @PrimaryKey
    val userId: String,
    val persona: String = "",
    val biggestMealTime: String = "00:00",
    val sleepTime: String = "00:00",
    val wakeUpTime: String = "00:00",
    val fruits: Boolean = false,
    val redMeat: Boolean = false,
    val fish: Boolean = false,
    val vegetables: Boolean = false,
    val seafood: Boolean = false,
    val eggs: Boolean = false,
    val grains: Boolean = false,
    val poultry: Boolean = false,
    val nutsOrSeeds: Boolean = false
)