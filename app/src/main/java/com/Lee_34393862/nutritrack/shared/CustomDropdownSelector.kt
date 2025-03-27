package com.Lee_34393862.nutritrack.shared

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownSelector(
    items: List<String>,
    label: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    value: String,
    onValueChange: (String) -> Unit,
) {
    TextField(
        value = value,
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        trailingIcon = {
            IconButton(onClick = { onExpandedChange(true) }) {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
            DropdownMenu(
                offset = DpOffset(x = (-64).dp, y = 0.dp),
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                items.forEachIndexed { index, item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            onValueChange(item)
                            onExpandedChange(false)
                        }
                    )
                    if (index != items.size - 1) {
                        HorizontalDivider()
                    }
                }
            }
        }
    )
}

