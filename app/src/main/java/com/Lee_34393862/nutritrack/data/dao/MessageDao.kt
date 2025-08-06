package com.Lee_34393862.nutritrack.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.Lee_34393862.nutritrack.data.entities.Message
import kotlinx.coroutines.flow.Flow


/**
 * This interface defines the data access object (DAO) for the message entity
 */
@Dao
interface MessageDao {
    @Insert
    suspend fun insert(message: Message)

    @Update
    suspend fun update(message: Message)

    @Delete
    suspend fun delete(message: Message)

    @Query("SELECT * FROM nutricoachtips WHERE userId = :userId")
    fun getMessagesByUserId(userId: String): Flow<List<Message>?>

    @Query("DELETE FROM nutricoachtips")
    suspend fun deleteAllMessages()
}