package com.Lee_34393862.nutritrack.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.Lee_34393862.nutritrack.data.PatientRepository
import com.Lee_34393862.nutritrack.shared.CustomProgressBar

data class FoodScore(val name: String, val score: Float, val scoreMax: Float)

@Composable
fun InsightsScreen(
    innerPadding: PaddingValues,
    patientRepository: PatientRepository
) {

    val foodScoreList: List<FoodScore> = listOf(
        FoodScore(
            name = "Vegetables",
            score = patientRepository.queryFoodScore("Vegetables").toFloat(),
            scoreMax = 10.0f
        ),
        FoodScore(
            name = "Fruits",
            score = patientRepository.queryFoodScore("Fruit").toFloat(),
            scoreMax = 10.0f
        ),
        FoodScore(
            name = "Grains & Cereals",
            score = patientRepository.queryFoodScore("Grainsandcereals").toFloat(),
            scoreMax = 5.0f
        ),
        FoodScore(
            name = "Whole Grains",
            score = patientRepository.queryFoodScore("Wholegrains").toFloat(),
            scoreMax = 5.0f
        ),
        FoodScore(
            name = "Meat & Alternatives",
            score = patientRepository.queryFoodScore("Meatandalternatives").toFloat(),
            scoreMax = 10.0f
        ),
        FoodScore(
            name = "Dairy",
            score = patientRepository.queryFoodScore("Dairyandalternatives").toFloat(),
            scoreMax = 10.0f
        ),
        FoodScore(
            name = "Water",
            score = patientRepository.queryFoodScore("Water").toFloat(),
            scoreMax = 5.0f
        ),
        FoodScore(
            name = "Unsaturated Fats",
            score = patientRepository.queryFoodScore("UnsaturatedFat").toFloat(),
            scoreMax = 5.0f
        ),
        FoodScore(
            name = "Saturated Fats",
            score = patientRepository.queryFoodScore("SaturatedFat").toFloat(),
            scoreMax = 5.0f
        ),
        FoodScore(
            name = "Sodium",
            score = patientRepository.queryFoodScore("Sodium").toFloat(),
            scoreMax = 10.0f
        ),
        FoodScore(
            name = "Sugar",
            score = patientRepository.queryFoodScore("Sugar").toFloat(),
            scoreMax = 10.0f
        ),
        FoodScore(
            name = "Alcohol",
            score = patientRepository.queryFoodScore("Alcohol").toFloat(),
            scoreMax = 5.0f
        ),
        FoodScore(
            name = "Discretionary Foods",
            score = patientRepository.queryFoodScore("Discretionary").toFloat(),
            scoreMax = 10.0f
        )
    )
    
    Column (
        modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)
            .fillMaxWidth()
    ){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Insights: Food Score",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
            )
        }
        Spacer(modifier = Modifier.size(size = 24.dp))
        foodScoreList.forEach { foodScore ->
            CustomProgressBar(
                label = foodScore.name,
                progressValue = foodScore.score,
                progressMax = foodScore.scoreMax
            )
        }


    }
}