package com.Lee_34393862.nutritrack.data.network

data class FruityViceResponseModel(
    val name: String,
    val id: Int,
    val family: String,
    val order: String,
    val genus: String,
    val nutritions: Nutritions
)

data class Nutritions(
    val calories: Double,
    val fat: Double,
    val sugar: Double,
    val carbohydrates: Double,
    val protein: Double
)
