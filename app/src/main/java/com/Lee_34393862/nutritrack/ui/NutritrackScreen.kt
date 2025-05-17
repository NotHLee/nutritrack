package com.Lee_34393862.nutritrack.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.Lee_34393862.nutritrack.shared.CustomSearchBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.Lee_34393862.nutritrack.data.viewmodel.NutritrackViewModel
import kotlinx.coroutines.launch

@Composable
fun NutritrackScreen(
    innerPadding: PaddingValues,
    viewModel: NutritrackViewModel,
) {

    val scope = rememberCoroutineScope()
    val fruitNameSuggestions by viewModel.fruitNameSuggestions.collectAsState()
    var query by remember { mutableStateOf<String>("") }
    val filteredFruitSuggestions by remember {
        derivedStateOf {
            if (query.isBlank()) {
                fruitNameSuggestions
            } else {
                fruitNameSuggestions.filter { it.contains(query, ignoreCase = true) }
            }
        }
    }

    Column (
        modifier = Modifier.padding(innerPadding)
    ){
        Text("Nutritrack")
        CustomSearchBar(
            query = query,
            onQueryChange = { query = it },
            onSearch = { currentQuery ->
                scope.launch {
                    viewModel.searchFruit(currentQuery)
                }
            },
            onResultClick = { result ->
                query = result
                scope.launch {
                    viewModel.searchFruit(result)
                }
            },
            searchResults = filteredFruitSuggestions

        )
    }
}