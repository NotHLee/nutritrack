package com.Lee_34393862.nutritrack.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Lee_34393862.nutritrack.data.viewmodel.SettingsViewModel
import com.Lee_34393862.nutritrack.shared.showErrorSnackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class AccountDetail(val icon: ImageVector, val text: String)
data class OtherSettingButtons(val icon: ImageVector, val text: String, val onClick: () -> Unit)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    innerPadding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    navigateToClinician: () -> Unit,
    viewModel: SettingsViewModel
) {

    val name by viewModel.currentUserName.collectAsState()
    val phoneNumber by viewModel.currentUserPhoneNumber.collectAsState()
    val userId by viewModel.currentUserId.collectAsState()

    val scope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var showBottomSheet by remember { mutableStateOf(false) }

    val accountDetails = remember(name, phoneNumber, userId) {
        mutableStateListOf<AccountDetail>(
            AccountDetail(Icons.Filled.Person, name),
            AccountDetail(Icons.Filled.Phone, phoneNumber),
            AccountDetail(Icons.Filled.Badge, userId)
        )
    }

    val otherSettingButtons = listOf<OtherSettingButtons>(
        OtherSettingButtons(
            icon = Icons.AutoMirrored.Filled.Logout,
            text = "Logout",
            onClick = { viewModel.logout() }
        ),
        OtherSettingButtons(
            icon = Icons.AutoMirrored.Filled.Login,
            text = "Clinician Login",
            onClick = { showBottomSheet = true }
        )
    )

    // show clinician login
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = modalBottomSheetState
        ) {
            ClinicianLoginSheet(
                toClinicianScreen = { navigateToClinician() },
                scope = scope,
                onLogin = { key -> viewModel.clinicianLogin(key) },
                onError = { error -> showErrorSnackbar(
                    snackbarHostState = snackbarHostState,
                    message = error
                ) }
            )
        }
    }

    Column (
        modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)
            .fillMaxWidth()
            .verticalScroll(state = rememberScrollState())
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


@Composable
fun ClinicianLoginSheet(
    toClinicianScreen: () -> Unit,
    scope: CoroutineScope,
    onLogin: suspend (String) -> Result<String>,
    onError: suspend (String) -> Unit
) {
    var key by remember { mutableStateOf<String>("") }
    var keyVisible by remember { mutableStateOf<Boolean>(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Clinician Login",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.size(16.dp))
        TextField(
            value = key,
            onValueChange = { key = it },
            label = { Text("Clinician Key") },
            placeholder = { Text("Enter your clinician key") },
            visualTransformation = if (keyVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            trailingIcon = {
                IconButton(
                    onClick = { keyVisible = !keyVisible }
                ) {
                    when (keyVisible) {
                        true -> Icon(Icons.Filled.VisibilityOff, contentDescription = "visible")
                        false -> Icon(Icons.Filled.Visibility, contentDescription = "visible off")
                    }
                }
            }
        )
        Spacer(modifier = Modifier.size(16.dp))
        Button(
            onClick = {
                scope.launch {
                    onLogin(key)
                        .onSuccess { _ ->
                            toClinicianScreen()
                        }
                        .onFailure { error ->
                            error.message?.let { onError(it) }
                        }
                }
            }
        ) {
            Text("Continue")
        }
    }
}
