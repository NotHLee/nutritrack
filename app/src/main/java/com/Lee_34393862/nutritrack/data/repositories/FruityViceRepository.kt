package com.Lee_34393862.nutritrack.data.repositories

import com.Lee_34393862.nutritrack.data.network.FruityViceAPIService
import com.Lee_34393862.nutritrack.data.network.FruityViceResponseModel

class FruityViceRepository() {
    private val apiService: FruityViceAPIService = FruityViceAPIService.create()

    suspend fun getFruitSuggestions(): Result<List<FruitSuggestion>> {
        val fruits: List<FruityViceResponseModel>? = apiService.getAllFruit().body()
        if (!fruits.isNullOrEmpty()) {
            val fruitSuggestions= fruits.map { fruit ->
                FruitSuggestion(fruit.id, fruit.name)
            }.sortedBy { it.name }
            return Result.success(fruitSuggestions)
        } else {
            return Result.failure(Exception("No fruits suggestions are found"))
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