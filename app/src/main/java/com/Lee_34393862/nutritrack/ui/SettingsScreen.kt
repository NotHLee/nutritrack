package com.Lee_34393862.nutritrack.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.Lee_34393862.nutritrack.data.viewmodel.SettingsViewModel
import androidx.compose.runtime.getValue

@Composable
fun SettingsScreen(
    innerPadding: PaddingValues,
    viewModel: SettingsViewModel
) {

    val name by viewModel.currentUserName.collectAsState()
    val phoneNumber by viewModel.currentUserPhoneNumber.collectAsState()
    val userId by viewModel.currentUserId.collectAsState()

    Column (
        modifier = Modifier.padding(innerPadding)
    ){
        Text(name)
        Text(phoneNumber)
        Text(userId)
        Button(
            onClick = {
                viewModel.logout()
            }
        ) {
            Text("logout")
        }
    }
}