package com.Lee_34393862.nutritrack.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Lee_34393862.nutritrack.data.AuthManager
import com.Lee_34393862.nutritrack.data.User
import kotlinx.coroutines.flow.StateFlow

class InsightsViewModel(context: Context): ViewModel() {
    // TODO: list out each user attribute instead of using a generic user object
    val currentUser: StateFlow<User?> get() = AuthManager.currentUser

    class InsightsViewModelFactory(context: Context): ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T: ViewModel> create(modelClass: Class<T>): T =
            InsightsViewModel(context) as T
    }
}