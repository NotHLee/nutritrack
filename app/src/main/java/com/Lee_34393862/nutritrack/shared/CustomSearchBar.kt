package com.Lee_34393862.nutritrack.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    searchResults: List<String>,
    onResultClick: (String) -> Unit,
    isLoading: Boolean = false,
) {
    DockedSearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { traversalIndex = 0f }
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
        ,
        shadowElevation = 6.dp,
        query = query,
        onQueryChange = onQueryChange,
        onSearch = {
            onSearch(query)
            // this should collapse the search bar after search
            // however, it doesn't work if you hit enter with the keyboard on an android emulator
            // only using the on screen keyboard's search button works since it listens to ImeAction.Search
            onExpandedChange(false)
        },
        active = expanded,
        onActiveChange = {
            onExpandedChange(it)
        },
        placeholder = {
            Text("Search for fruit")
        },
        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = "search")
        },
    ) {
        when (isLoading) {
            true -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            false -> LazyColumn {
                items(count = searchResults.size) { index ->
                    val resultText = searchResults[index]
                    ListItem(
                        headlineContent = { Text(resultText) },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier = Modifier
                            .clickable {
                                onResultClick(resultText)
                                onExpandedChange(false)
                            }
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}