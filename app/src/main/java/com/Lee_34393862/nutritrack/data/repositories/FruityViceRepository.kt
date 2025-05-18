package com.Lee_34393862.nutritrack.data.repositories

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.rememberCoroutineScope
import com.Lee_34393862.nutritrack.data.network.FruityViceAPIService
import com.Lee_34393862.nutritrack.data.network.FruityViceResponseModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FruityViceRepository(
    private val scope: CoroutineScope
) {
    private val apiService: FruityViceAPIService = FruityViceAPIService.create()

    // cache fruit name suggestions for search bar in nutritrack
    private var _fruitNameSuggestions = MutableStateFlow<List<FruitSuggestion>>(emptyList())
    val fruitNameSuggestions: StateFlow<List<FruitSuggestion>> = _fruitNameSuggestions.asStateFlow()

    init {
        scope.launch {
            loadFruitSuggestions()
        }
    }

    suspend fun loadFruitSuggestions() {
        val fruits: List<FruityViceResponseModel>? = apiService.getAllFruit().body()
        Log.d("fruits", fruits.toString())
        if (!fruits.isNullOrEmpty()) {
            _fruitNameSuggestions.value = fruits.map { fruit ->
                FruitSuggestion(fruit.id, fruit.name)
            }.sortedBy { it.name }
        }
    }

    suspend fun getFruit(fruitId: Int): Result<FruityViceResponseModel> {
        val fruit: FruityViceResponseModel? = apiService.getFruit(fruitId).body()

        return when (fruit) {
            null -> Result.failure(Exception("Fruit does not exist"))
            else -> Result.success(fruit)
        }
    }
}

data class FruitSuggestion(val id: Int, val name: String)