package com.Lee_34393862.nutritrack.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Lee_34393862.nutritrack.R
import com.Lee_34393862.nutritrack.data.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    viewModel: HomeViewModel,
    navigateToQuestion: () -> Unit,
    navigateToInsights: () -> Unit
) {

    val currentUserName by viewModel.currentUserName.collectAsState()
    val currentUserTotalFoodScore by viewModel.currentUserTotalFoodScore.collectAsState()

    Column(
        modifier = Modifier.padding(16.dp).padding(innerPadding)
    ) {
        Greetings(
            name = currentUserName,
            navigateToEdit = { navigateToQuestion() }
        )
        Image(
            painter = painterResource(R.drawable.foodplate),
            contentDescription = "foodplate",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.height(256.dp)
        )
        MyScore(
            foodScore = currentUserTotalFoodScore.toString(),
            navigateToInsights = { navigateToInsights() }
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
        InfoScore()
    }
}

@Composable
fun Greetings(
    name: String,
    navigateToEdit: () -> Unit
) {
    Text(
        "Hello,",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        color = Color.Gray
    )
    Text(
        name,
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp
    )
    Spacer(modifier = Modifier.size(8.dp))
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ){
        Text(
            "You already filled in your Food Intake Questionnaire, but you can change details here:",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth(0.70f),
            textAlign = TextAlign.Justify
        )
        Button(
            onClick = { navigateToEdit() },
            shape = RoundedCornerShape(size = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Default.Edit, contentDescription = "edit", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.size(4.dp))
                Text("Edit", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
fun MyScore(
    foodScore: String,
    navigateToInsights: () -> Unit
) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ){
        Text(
            "My Score",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold
        )
        TextButton(
            onClick = { navigateToInsights() },
            modifier = Modifier.offset(x = 16.dp)
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
            ){
                Text(
                    "See all scores",
                    style = MaterialTheme.typography.labelLarge
                )
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "right",
                )
            }
        }
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.tertiaryContainer),
            ) {
                Icon(
                    when (foodScore.toFloat() > 50) {
                        true -> Icons.Default.KeyboardArrowUp
                        false -> Icons.Default.KeyboardArrowDown
                    },
                    contentDescription = "arrow_up",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(30.dp),
                    tint = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.tertiaryContainer)
                )
            }
            Spacer(Modifier.size(size = 16.dp))
            Text(
                "Your Food Quality score",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        Text(
            "$foodScore/100",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.tertiaryContainer)
        )
    }

}

@Composable
fun InfoScore() {

    Text(
        "What is the Food Quality Score?",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.size(4.dp))
    Text(
        "Your Food Quality Score provides a snapshot of how well your" +
                " eating patterns align with established food guidelines, helping" +
                " you identify both strengths and opportunities for improvement" +
                " in your diet",
        style = MaterialTheme.typography.bodyMedium,
    )
    Spacer(modifier = Modifier.size(8.dp))
    Text(
        "This personalized measurement considers various food groups " +
                "including vegetables, fruits, whole grains, and proteins to give " +
                "you practical insights for making healthier food choices",
        style = MaterialTheme.typography.bodyMedium,
    )


}