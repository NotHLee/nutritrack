package com.Lee_34393862.nutritrack.data.viewmodel

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.Lee_34393862.nutritrack.data.network.FruityViceResponseModel
import com.Lee_34393862.nutritrack.data.repositories.FruityViceRepository
import kotlinx.coroutines.flow.StateFlow

class NutritrackViewModel(
    private val fruityViceRepository: FruityViceRepository
) : ViewModel() {

    val fruitNameSuggestions: StateFlow<List<String>> = fruityViceRepository.fruitNameSuggestions

    suspend fun searchFruit(fruitName: String) {
        fruityViceRepository.getFruit(fruitName)
            .onSuccess { fruit ->
                Log.d("fruit", fruit.toString())
            }
            .onFailure { error ->
                Log.d("error", error.message ?: "")
            }
    }
}