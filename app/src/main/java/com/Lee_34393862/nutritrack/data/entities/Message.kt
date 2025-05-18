package com.Lee_34393862.nutritrack.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(entity = Patient::class,
        parentColumns = arrayOf("userId"),
        childColumns = arrayOf("userId"),
        onDelete = ForeignKey.CASCADE
    )],
    tableName = "message")
data class Message(
    @PrimaryKey(autoGenerate = true)
    val messageId: Int = 0,
    val userId: String,
    val response: String,
)
