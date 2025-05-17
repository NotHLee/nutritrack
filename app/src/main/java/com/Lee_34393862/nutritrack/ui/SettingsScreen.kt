package com.Lee_34393862.nutritrack.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.Lee_34393862.nutritrack.data.viewmodel.SettingsViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class AccountDetail(val icon: ImageVector, val text: String)
data class OtherSettingButtons(val icon: ImageVector, val text: String, val onClick: () -> Unit)

@Composable
fun SettingsScreen(
    innerPadding: PaddingValues,
    viewModel: SettingsViewModel
) {

    val name by viewModel.currentUserName.collectAsState()
    val phoneNumber by viewModel.currentUserPhoneNumber.collectAsState()
    val userId by viewModel.currentUserId.collectAsState()

    val accountDetails = remember(name, phoneNumber, userId) {
        mutableStateListOf<AccountDetail>(
            AccountDetail(Icons.Filled.Person, name),
            AccountDetail(Icons.Filled.Phone, phoneNumber),
            AccountDetail(Icons.Filled.Badge, userId)
        )
    }

    val otherSettingButtons = listOf<OtherSettingButtons>(
        OtherSettingButtons(
            Icons.AutoMirrored.Filled.Logout,
            "Logout"
        ) { viewModel.logout() },
        OtherSettingButtons(
            Icons.AutoMirrored.Filled.Login,
            "Clinician Login"
        ) { }
    )

    Column (
        modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)
            .fillMaxWidth()
    ){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Settings",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )
        }
        Text(
            "Account",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.size(16.dp))

        // account detail items
        accountDetails.forEach { detail ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(detail.icon, contentDescription = detail.text, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    detail.text,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
        Text(
            "Other settings",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray
        )

        // buttons in other settings
        otherSettingButtons.forEach { setting ->
            TextButton(
                onClick = {
                    setting.onClick()
                }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            setting.icon,
                            contentDescription = setting.text,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        Text(
                            setting.text,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "arrow right",
                        modifier = Modifier.size(28.dp)
                    )
                }

            }
        }
    }
}
