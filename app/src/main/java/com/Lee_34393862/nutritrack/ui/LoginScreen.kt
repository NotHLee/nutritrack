package com.Lee_34393862.nutritrack.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Lee_34393862.nutritrack.R
import com.Lee_34393862.nutritrack.data.viewmodel.LoginScreenState
import com.Lee_34393862.nutritrack.data.viewmodel.LoginViewModel
import com.Lee_34393862.nutritrack.shared.CustomDropdownSelector
import com.Lee_34393862.nutritrack.shared.CustomPasswordTextField
import com.Lee_34393862.nutritrack.shared.CustomSnackbarHost
import com.Lee_34393862.nutritrack.shared.showErrorSnackbar
import com.Lee_34393862.nutritrack.shared.showSuccessSnackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LoginScreen(
    viewModel: LoginViewModel,
    navigateToQuestions: () -> Unit
) {

    val patientIds by viewModel.patientIds.collectAsState()
    val isLoadingState by viewModel.isLoadingState.collectAsState()
    val scope = rememberCoroutineScope()
    val bottomSheetScaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    var registerMode by remember { mutableStateOf<Boolean>(false) }

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            if (registerMode)
                RegisterSheet (
                    scope = scope,
                    patientIds = patientIds,
                    isLoadingState = isLoadingState,
                    onLogin = { registerMode = false },
                    onRegister = { userId,
                                   name,
                                   phoneNumber,
                                   password,
                                   confirmPassword ->
                        viewModel.register(userId, name, phoneNumber, password, confirmPassword)
                     },
                    onSuccess = { success ->
                        registerMode = false
                        showSuccessSnackbar(
                            snackbarHostState = bottomSheetScaffoldState.snackbarHostState,
                            message = success
                        )
                    },
                    onError = { error ->
                        showErrorSnackbar(
                            snackbarHostState = bottomSheetScaffoldState.snackbarHostState,
                            message = error
                        )
                    }
                )
            else {
                LoginSheet(
                    navigateToQuestions = { navigateToQuestions() },
                    scope = scope,
                    patientIds = patientIds,
                    isLoadingState = isLoadingState,
                    onLogin = { userId, password -> viewModel.login(userId, password) },
                    onRegister = { registerMode = true },
                    onError = { error ->
                        showErrorSnackbar(
                            snackbarHostState = bottomSheetScaffoldState.snackbarHostState,
                            message = error
                        )
                    }
                )
            }
        },
        sheetPeekHeight = 0.dp,
        snackbarHost = {
            CustomSnackbarHost(snackbarHostState = bottomSheetScaffoldState.snackbarHostState)
        }
    ) { innerPadding ->
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

                // only allow user to press login when all user ids are loaded
                if (isLoadingState == LoginScreenState.InitialLoading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = {
                            scope.launch {
                                bottomSheetScaffoldState.bottomSheetState.expand()
                            }
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
fun LoginSheet(
    navigateToQuestions: () -> Unit,
    scope: CoroutineScope,
    patientIds: List<String>,
    isLoadingState: LoginScreenState,
    onLogin: suspend (String, String) -> Result<String>,
    onRegister: () -> Unit,
    onError: suspend (String) -> Unit
) {
    var loginSheetDropdownExpanded by remember { mutableStateOf<Boolean>(false) }
    var userId by remember { mutableStateOf<String>("") }
    var password by remember { mutableStateOf<String>("") }
    var passwordVisible by remember { mutableStateOf<Boolean>(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
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
            items = patientIds,
            label = "My ID (Provided by your clinician)",
            expanded = loginSheetDropdownExpanded,
            onExpandedChange = { loginSheetDropdownExpanded = it },
            value = userId,
            onValueChange = { userId = it },
        )
        Spacer(modifier = Modifier.size(16.dp))
        CustomPasswordTextField(
            labelText = "Password",
            password = password,
            onPasswordChange = { password = it },
            passwordVisible = passwordVisible,
            onToggleVisiblity = { passwordVisible = !passwordVisible }
        )
        Text(
            text = "This app is only for pre-registered users. Please have your ID and phone number handy before continuing",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(16.dp)
        )

        if (isLoadingState == LoginScreenState.LoginLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    scope.launch {
                        onLogin(userId, password)
                            .onSuccess { _ ->
                                navigateToQuestions()
                            }
                            .onFailure { error ->
                                error.message?.let { onError(it) }
                            }
                    }
                }
            ) {
                Text("Continue")
            }
            Button(
                onClick = { onRegister() }
            ) {
                Text("Register")
            }
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
fun RegisterSheet(
    scope: CoroutineScope,
    patientIds: List<String>,
    isLoadingState: LoginScreenState,
    onLogin: () -> Unit,
    onRegister: suspend (String, String, String, String, String) -> Result<String>,
    onSuccess: suspend (String) -> Unit,
    onError: suspend (String) -> Unit
) {
    var loginSheetDropdownExpanded by remember { mutableStateOf<Boolean>(false) }
    var userId by remember { mutableStateOf<String>("") }
    var name by remember { mutableStateOf<String>("") }
    var phoneNumber by remember { mutableStateOf<String>("") }
    var password by remember { mutableStateOf<String>("") }
    var confirmPassword by remember { mutableStateOf<String>("") }
    var passwordVisible by remember { mutableStateOf<Boolean>(false) }
    var confirmPasswordVisible by remember { mutableStateOf<Boolean>(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Register",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.size(16.dp))
        CustomDropdownSelector(
            items = patientIds,
            label = "My ID (Provided by your clinician)",
            expanded = loginSheetDropdownExpanded,
            onExpandedChange = { loginSheetDropdownExpanded = it },
            value = userId,
            onValueChange = { userId = it },
        )
        Spacer(modifier = Modifier.size(16.dp))
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.size(16.dp))
        TextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.size(16.dp))
        CustomPasswordTextField(
            labelText = "Password",
            password = password,
            onPasswordChange = { password = it },
            passwordVisible = passwordVisible,
            onToggleVisiblity = { passwordVisible = !passwordVisible }
        )
        Spacer(modifier = Modifier.size(16.dp))
        CustomPasswordTextField(
            labelText = "Confirm Password",
            password = confirmPassword,
            onPasswordChange = { confirmPassword = it },
            passwordVisible = confirmPasswordVisible,
            onToggleVisiblity = { confirmPasswordVisible = !confirmPasswordVisible }
        )
        Text(
            text = "This app is only for pre-registered users. Please have your ID and phone number handy before continuing",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(16.dp)
        )
        if (isLoadingState == LoginScreenState.RegisterLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    scope.launch {
                        onRegister(userId, name, phoneNumber, password, confirmPassword)
                            .onSuccess { success ->
                                onSuccess(success)
                            }
                            .onFailure { error ->
                                error.message?.let { onError(it) }
                            }
                    }
                }
            ) {
                Text("Register")
            }
            Button(
                onClick = {
                    onLogin()
                }
            ) {
                Text("Login")
            }
        }
    }
}