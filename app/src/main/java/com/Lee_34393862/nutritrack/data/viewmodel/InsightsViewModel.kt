package com.Lee_34393862.nutritrack.data.viewmodel

import androidx.lifecycle.ViewModel
import com.Lee_34393862.nutritrack.data.repositories.User
import com.Lee_34393862.nutritrack.data.repositories.UserRepository
import kotlinx.coroutines.flow.StateFlow

class InsightsViewModel(
    private val userRepository: UserRepository
): ViewModel() {
    val currentUser: StateFlow<User?> get() = userRepository.currentUser
}