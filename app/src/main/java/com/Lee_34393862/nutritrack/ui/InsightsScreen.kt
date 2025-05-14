package com.Lee_34393862.nutritrack.ui

import android.content.Intent
import android.content.Intent.ACTION_SEND
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.Lee_34393862.nutritrack.data.PatientRepository
import com.Lee_34393862.nutritrack.shared.CustomLabelledProgressBar
import com.Lee_34393862.nutritrack.shared.CustomProgressBar

data class FoodScore(val name: String, val score: Float, val scoreMax: Float)

@Composable
fun InsightsScreen(
    innerPadding: PaddingValues,
    patientRepository: PatientRepository,
    navigateToNutritrack: () -> Unit
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
    val context = LocalContext.current

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
        Spacer(modifier = Modifier.size(size = 8.dp))
        foodScoreList.forEach { foodScore ->
            CustomLabelledProgressBar(
                label = foodScore.name,
                progressValue = foodScore.score,
                progressMax = foodScore.scoreMax
            )
        }
        Spacer(modifier = Modifier.size(size = 8.dp))
        Text(
            "Total Food Quality Score",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.size(size = 4.dp))
        Row {
            CustomProgressBar(
                progressValue = patientRepository.getTotalFoodScore().toFloat(),
                progressMax = 100f,
                modifier = Modifier.weight(1f)
            )
            Text(
                "${patientRepository.getTotalFoodScore().toFloat()}/100",
                textAlign = TextAlign.End,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Button(
                onClick = {
                    val shareIntent = Intent(ACTION_SEND)
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hi, I just got a HEIFA score of ${patientRepository.getTotalFoodScore()}!"
                    )
                    startActivity(
                        context,
                        Intent.createChooser(shareIntent, "Share text via"),
                        null
                    )
                },
                shape = RoundedCornerShape(size = 8.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                Icon(Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.size(8.dp))
                Text("Share with someone")
            }
            Button(
                onClick = { navigateToNutritrack() },
                shape = RoundedCornerShape(size = 8.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                Icon(Icons.Default.Star, contentDescription = "Share", modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.size(8.dp))
                Text("Improve my diet")
            }
        }


    }
}