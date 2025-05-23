package com.Lee_34393862.nutritrack.shared

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(
    onScrollDisabledChange: (Boolean) -> Unit = {},
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    searchResults: List<String>,
    onResultClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    var expanded by rememberSaveable { mutableStateOf<Boolean>(false) }

    Box(
        Modifier
            .semantics { isTraversalGroup = true }
            .fillMaxWidth()
    ) {
        SearchBar(
            modifier = Modifier
                .then(if (!expanded) modifier else Modifier)
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f }
            ,
            shadowElevation = 6.dp,
            query = query,
            onQueryChange = onQueryChange,
            onSearch = {
                onSearch(query)
                expanded = false
            },
            active = expanded,
            onActiveChange = {
                onScrollDisabledChange(true)
                expanded = it
             },
            placeholder = {
                Text("Search for fruit")
            },
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = "search")
            },
        ) {

            // if an expanded search bar is in another scrollable column, it will crash due to nested scrolling
            // a workaround is to simply disable the parent column's scroll temporarily and allow the scroll behaviour of the lazycolumn
            // only after the lazy column is not rendered, we will reenable the parent column's scroll behaviour
            DisposableEffect(Unit) {
                onDispose {
                    onScrollDisabledChange(false)
                }
            }

            LazyColumn() {
                items(count = searchResults.size) { index ->
                    val resultText = searchResults[index]
                    ListItem(
                        headlineContent = { Text(resultText) },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier = Modifier
                            .clickable {
                                onResultClick(resultText)
                                expanded = false
                            }
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}