package com.Lee_34393862.nutritrack.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Lee_34393862.nutritrack.data.repositories.User
import com.Lee_34393862.nutritrack.data.repositories.UserRepository
import kotlinx.coroutines.flow.StateFlow

class InsightsViewModel(context: Context, private val userRepository: UserRepository): ViewModel() {
    // TODO: list out each user attribute instead of using a generic user object
    val currentUser: StateFlow<User?> get() = userRepository.currentUser

    class InsightsViewModelFactory(context: Context, private val userRepository: UserRepository): ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T: ViewModel> create(modelClass: Class<T>): T =
            InsightsViewModel(context, userRepository) as T
    }
}