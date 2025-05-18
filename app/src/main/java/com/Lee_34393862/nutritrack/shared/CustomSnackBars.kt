package com.Lee_34393862.nutritrack.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.Lee_34393862.nutritrack.ui.theme.errorContainerDark

@Composable
fun CustomErrorSnackBar(
    message: String
) {
    Snackbar(containerColor = errorContainerDark) {
        Row (
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ){
            Icon(
                Icons.Rounded.Clear,
                contentDescription = "error",
            )
            Spacer(Modifier.size(8.dp))
            Text(
                message,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
fun CustomSuccessSnackBar(
    message: String
) {
    Snackbar(containerColor = Color.Green) {
        Row (
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ){
            Icon(
                Icons.Rounded.CheckCircle,
                contentDescription = "success",
            )
            Spacer(Modifier.size(8.dp))
            Text(
                message,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}
