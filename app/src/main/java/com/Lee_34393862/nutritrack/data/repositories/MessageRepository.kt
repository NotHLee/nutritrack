package com.Lee_34393862.nutritrack.data.repositories

import android.content.Context
import com.Lee_34393862.nutritrack.data.AppDatabase
import com.Lee_34393862.nutritrack.data.dao.MessageDao
import com.Lee_34393862.nutritrack.data.entities.Message
import kotlinx.coroutines.flow.Flow

class MessageRepository {

    val messageDao: MessageDao

    constructor(context: Context) {
        messageDao = AppDatabase.getDatabase(context).messageDao()
    }

    suspend fun insert(message: Message) {
        messageDao.insert(message)
    }

    suspend fun update(message: Message) {
        messageDao.update(message)
    }

    suspend fun delete(message: Message) {
        messageDao.delete(message)
    }

    fun getMessagesByUserId(userId: String): Flow<List<Message>?> {
        return messageDao.getMessagesByUserId(userId)
    }

    suspend fun deleteAllMessages(userId: String) {
        messageDao.deleteAllMessages()
    }
}