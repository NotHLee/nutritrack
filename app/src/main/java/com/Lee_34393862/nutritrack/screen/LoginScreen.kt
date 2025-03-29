package com.Lee_34393862.nutritrack.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.Lee_34393862.nutritrack.R
import com.Lee_34393862.nutritrack.Screens
import com.Lee_34393862.nutritrack.data.PatientRepository
import com.Lee_34393862.nutritrack.shared.CustomDropdownSelector
import com.Lee_34393862.nutritrack.ui.theme.errorContainerDark
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LoginScreen(
    patientRepository: PatientRepository,
    navController: NavHostController,
) {

    // ui states
    var userId: String by remember { mutableStateOf("") }
    var phoneNumber: String by remember { mutableStateOf("") }
    var loginExpanded: Boolean by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        // login sheet
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
        if (loginExpanded) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { loginExpanded = false },
                ) {
                LoginSheet(
                    navController = navController,
                    patientRepository = patientRepository,
                    userId = userId,
                    phoneNumber = phoneNumber,
                    onUserIdChange = { value -> userId = value },
                    onPhoneNumberChange = { value -> phoneNumber = value},
                )
            }
        }

        // main content
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Nutritrack ",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 48.sp
                )
                Icon(
                    painter = painterResource(id = R.drawable.clipart139948),
                    contentDescription = "Nutritrack Logo",
                    modifier = Modifier.padding(16.dp),
                    tint = Color.Unspecified
                )
                DisclaimerText()
                Button(
                    onClick = {
                        loginExpanded = true
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(48.dp)
                ) {
                    Text("Login")
                }
            }
            Text(
                text = "Designed by Lee Hao Yang (34393862)",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                modifier = Modifier
                    .align(alignment = Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            )
        }
    }
}

@Composable
fun DisclaimerText() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "This app provides general health and nutrition information for educational purposes only.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic
        )
        Text(
            text = "It is not intended as medical advice, diagnosis, or treatment.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic
        )
        Text(
            text = "Always consult a qualified healthcare professional before making any changes to your diet, exercise, or health regimen.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic
        )
        Text(
            text = "Use this app at your own risk.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic
        )
        Text(
            text = "If you’d like to an Accredited Practicing Dietitian (APD), please visit the Monash Nutrition/Dietetics Clinic (discounted rates for students):",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic
        )
        ClickableLink()
    }
}

@Composable
fun ClickableLink() {
    val url = "https://www.monash.edu/medicine/scs/nutrition/clinics/nutrition"

    val annotatedString = buildAnnotatedString {
        pushStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
                fontStyle = FontStyle.Italic
            )
        )
        append(url)
        addLink(
            LinkAnnotation.Url(url = url),
            start = 0,
            end = url.length
        )
        pop()
    }

    Text(
        text = annotatedString,
        style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun LoginSheet(
    navController: NavHostController,
    patientRepository: PatientRepository,
    userId: String,
    onUserIdChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
) {
    var dropdownExpanded: Boolean by remember { mutableStateOf(false) }
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val scope: CoroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                CustomErrorSnackBar(data.visuals.message)
            }
        }
    )
    { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.size(16.dp))

            CustomDropdownSelector(
                items = patientRepository.getAllUserId().getOrThrow(),
                label = "My ID (Provided by your clinician)",
                expanded = dropdownExpanded,
                onExpandedChange = { dropdownExpanded = it },
                value = userId,
                onValueChange = { onUserIdChange(it) },
            )

            Spacer(modifier = Modifier.size(16.dp))
            TextField(
                value = phoneNumber,
                onValueChange = { onPhoneNumberChange(it) },
                label = { Text("Phone Number") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Text(
                text = "This app is only for pre-registered users. Please have your ID and phone number handy before continuing",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(16.dp)
            )
            Button(
                onClick = {
                    patientRepository.authenticate(userId, phoneNumber)
                        .onSuccess { _ ->
                            navController.navigate(Screens.Question.route)
                        }
                        .onFailure { error ->
                            scope.launch {
                                error.message?.let { snackbarHostState.showSnackbar(it) }
                            }
                        }
                }
            ) {
                Text("Continue")
            }
        }
    }
}

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