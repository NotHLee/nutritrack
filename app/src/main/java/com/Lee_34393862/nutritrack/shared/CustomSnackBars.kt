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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.Lee_34393862.nutritrack.ui.theme.errorContainerDark

sealed class SnackbarType(val identifier: String) {
    object Success : SnackbarType("success")
    object Error: SnackbarType("error")
}

suspend fun showSuccessSnackbar(
    snackbarHostState: SnackbarHostState,
    message: String
) {
    snackbarHostState.currentSnackbarData?.dismiss()
    snackbarHostState.showSnackbar(
        message = message,
        actionLabel = SnackbarType.Success.identifier,
        duration = SnackbarDuration.Short
    )
}

suspend fun showErrorSnackbar(
    snackbarHostState: SnackbarHostState,
    message: String
) {
    snackbarHostState.currentSnackbarData?.dismiss()
    snackbarHostState.showSnackbar(
        message = message,
        actionLabel = SnackbarType.Error.identifier,
        duration = SnackbarDuration.Short
    )
}

@Composable
fun CustomSnackbarHost(
    snackbarHostState: SnackbarHostState
) {
    SnackbarHost(hostState = snackbarHostState) { data ->
        data.visuals.actionLabel?.let { label ->
            if (label == "success") {
                CustomSuccessSnackBar(data.visuals.message)
            } else {
                CustomErrorSnackBar(data.visuals.message)
            }
        }
    }
}

@Composable
fun CustomErrorSnackBar(
    message: String
) {
    Snackbar(containerColor = errorContainerDark) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
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
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
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
