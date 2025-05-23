package com.Lee_34393862.nutritrack.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import com.Lee_34393862.nutritrack.data.network.FruityViceResponseModel
import com.Lee_34393862.nutritrack.data.repositories.FruitSuggestion
import com.Lee_34393862.nutritrack.data.viewmodel.NutritrackViewModel
import com.Lee_34393862.nutritrack.shared.CustomSearchBar
import com.Lee_34393862.nutritrack.shared.StreamingText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


data class FruitDetailsText(val label: String, val value: String)

@Composable
fun NutritrackScreen(
    innerPadding: PaddingValues,
    viewModel: NutritrackViewModel,
) {
    val scope = rememberCoroutineScope()
    val fruitSuggestions by viewModel.fruitSuggestion.collectAsState()
    val fruitDetails by viewModel.fruitDetails.collectAsState()
    val currentMotivationalMessage by viewModel.currentMotivationalMessage.collectAsState()
    val motivationalMessages by viewModel.motivationalMessages.collectAsState()
    var query by remember { mutableStateOf<String>("") }
    val filteredFruitSuggestions by remember {
        derivedStateOf {
            if (query.isBlank()) {
                fruitSuggestions
            } else {
                fruitSuggestions.filter { it.name.contains(query, ignoreCase = true) }
            }
        }
    }
    val fruitDetailsTexts by remember(fruitDetails) {
        derivedStateOf {
            fruitDetails?.let { details ->
                listOfNotNull(
                    FruitDetailsText("Family", details.family),
                    FruitDetailsText("Genus", details.genus),
                    FruitDetailsText("Order", details.order),
                    FruitDetailsText("Calories", "${details.nutritions.calories} kcal"),
                    FruitDetailsText("Fat", "${details.nutritions.fat}g"),
                    FruitDetailsText("Sugar", "${details.nutritions.sugar}g"),
                    FruitDetailsText("Carbohydrates", "${details.nutritions.carbohydrates}g"),
                    FruitDetailsText("Protein", "${details.nutritions.protein}g")
                )
            } ?: emptyList()
        }
    }
    var isMessageHistoryDialogOpen by remember { mutableStateOf<Boolean>(false) }

    // dialog to show message history
    if (isMessageHistoryDialogOpen) {
        MessageHistoryDialog(
            motivationalMessages = motivationalMessages,
            onDismissRequest = { isMessageHistoryDialogOpen = false }
        )
    }

    Column (
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ){
        when (fruitSuggestions.isEmpty()) {
            true -> AsyncImage(
                model = "https://picsum.photos/800/600",
                contentDescription = "Random Image",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
            )
            false ->
                FruitSearchSection(
                    scope = scope,
                    viewModel = viewModel,
                    query = query,
                    fruitSuggestions = fruitSuggestions,
                    filteredFruitSuggestions = filteredFruitSuggestions,
                    fruitDetailsTexts = fruitDetailsTexts,
                    fruitDetails = fruitDetails,
                    onQueryChange = { query = it },
                    onResultChange = { query = it }
                )
        }
        Spacer(modifier = Modifier.size(16.dp))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp))
        Button(
            onClick = { viewModel.generateMotivationalMessage() },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier.padding(horizontal = 24.dp),
            elevation = ButtonDefaults.buttonElevation(6.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Chat,
                contentDescription = "Chat",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text("Motivational Message (AI)")
        }
        Spacer(modifier = Modifier.size(8.dp))
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(bottom = 8.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                when (currentMotivationalMessage) {
                    null -> Text("")
                    else -> StreamingText(currentMotivationalMessage!!)
                }
            }
        }
        Button(
            onClick = { isMessageHistoryDialogOpen = true },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .align(Alignment.End),
            elevation = ButtonDefaults.buttonElevation(6.dp)
        ) {
            Icon(
                Icons.Filled.History,
                contentDescription = "Chat",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text("Show message history")
        }
    }
}

@Composable
fun MessageHistoryDialog(
    onDismissRequest: () -> Unit,
    motivationalMessages: List<String>
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "AI Tips",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f, false)
                    ) {
                        items(motivationalMessages) { message ->
                            Card(
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = message,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                }
                Button(
                    onClick = { onDismissRequest() },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .align(Alignment.End),
                    elevation = ButtonDefaults.buttonElevation(6.dp)
                ) {
                    Text("Done")
                }
            }
        }
    }
}

@Composable
fun FruitSearchSection(
    scope: CoroutineScope,
    viewModel: NutritrackViewModel,
    query: String,
    fruitSuggestions: List<FruitSuggestion>,
    filteredFruitSuggestions: List<FruitSuggestion>,
    fruitDetailsTexts: List<FruitDetailsText>,
    fruitDetails: FruityViceResponseModel?,
    onQueryChange: (String) -> Unit,
    onResultChange: (String) -> Unit,
) {
    val localDensity = LocalDensity.current

    CustomSearchBar(
        modifier = Modifier.padding(horizontal = 16.dp),
        query = query,
        onQueryChange = { onQueryChange(it) },
        onSearch = { currentQuery ->
            scope.launch {
                viewModel.searchFruit(
                    fruitSuggestions.find { fruitSuggestion ->
                        fruitSuggestion.name == currentQuery
                    }?.id ?: -1
                )
            }
        },
        onResultClick = { result ->
            onResultChange(result)
            scope.launch {
                viewModel.searchFruit(
                    fruitSuggestions.find { fruitSuggestion ->
                        fruitSuggestion.name == result
                    }?.id ?: -1
                )
            }
        },
        searchResults = filteredFruitSuggestions.map { fruitSuggestion -> fruitSuggestion.name }
    )
    Spacer (modifier = Modifier.size(24.dp))
    when (fruitDetails) {
        null ->
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    // placeholder column to get same height as card filled with data
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .alpha(0f)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            (0 until 3).forEach {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        "placeholder",
                                        modifier = Modifier.fillMaxWidth(0.3f),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        "placeholder",
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                            }

                            HorizontalDivider(
                                modifier = Modifier.padding(
                                    vertical = 8.dp,
                                    horizontal = 8.dp
                                )
                            )
                            Text(
                                "Nutritional Values",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                            // remaining details about nutrition
                            Spacer(modifier = Modifier.size(8.dp))
                            Column(modifier = Modifier.fillMaxWidth()) {
                                (0 until 5).forEach {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            "placeholder",
                                            modifier = Modifier.fillMaxWidth(0.5f),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            "placeholder",
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Text("No fruit is selected")
                }
            }
        else ->
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    // first 3 details about plant info
                    Column(modifier = Modifier.fillMaxWidth()) {
                        fruitDetailsTexts.take(3).forEach { details ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "${details.label}:",
                                    modifier = Modifier.fillMaxWidth(0.3f),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    details.value,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            vertical = 8.dp,
                            horizontal = 8.dp
                        )
                    )
                    Text(
                        "Nutritional Values",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    // remaining details about nutrition
                    Spacer(modifier = Modifier.size(8.dp))
                    Column(modifier = Modifier.fillMaxWidth()) {
                        fruitDetailsTexts.drop(3).forEach { details ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "${details.label}:",
                                    modifier = Modifier.fillMaxWidth(0.5f),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    details.value,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }
                }
            }
        }
}
