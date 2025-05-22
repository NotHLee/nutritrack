package com.Lee_34393862.nutritrack.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        colors = TextFieldDefaults.colors(
            disabledTextColor = TextFieldDefaults.colors().unfocusedTextColor,
            disabledContainerColor = TextFieldDefaults.colors().unfocusedContainerColor,
            disabledLeadingIconColor = TextFieldDefaults.colors().unfocusedLeadingIconColor,
            disabledTrailingIconColor = TextFieldDefaults.colors().unfocusedTrailingIconColor,
            disabledLabelColor = TextFieldDefaults.colors().unfocusedLabelColor,
            disabledPlaceholderColor = TextFieldDefaults.colors().unfocusedPlaceholderColor,
            disabledSupportingTextColor = TextFieldDefaults.colors().unfocusedSupportingTextColor,
            disabledPrefixColor = TextFieldDefaults.colors().unfocusedPrefixColor,
            disabledSuffixColor = TextFieldDefaults.colors().unfocusedSuffixColor
        ),
        enabled = false,
        value = value,
        onValueChange = {},
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = { onExpandedChange(true) }),
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

