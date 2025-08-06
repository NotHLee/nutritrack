package com.Lee_34393862.nutritrack.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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

    // sync the bottom sheet state with the view model
    LaunchedEffect(viewModel.isBottomSheetExpanded) {
        if(viewModel.isBottomSheetExpanded) {
            bottomSheetScaffoldState.bottomSheetState.expand()
        } else {
            bottomSheetScaffoldState.bottomSheetState.partialExpand()
        }
    }

    LaunchedEffect(bottomSheetScaffoldState.bottomSheetState.targetValue) {
        val isExpanded = bottomSheetScaffoldState.bottomSheetState.targetValue == SheetValue.Expanded
        viewModel.isBottomSheetExpanded = isExpanded
    }

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            if (viewModel.registerMode)
                RegisterSheet (
                    viewModel = viewModel,
                    scope = scope,
                    patientIds = patientIds,
                    isLoadingState = isLoadingState,
                    onLogin = { viewModel.registerMode = false },
                    onSuccess = { success ->
                        scope.launch {
                            showSuccessSnackbar(
                                snackbarHostState = bottomSheetScaffoldState.snackbarHostState,
                                message = success
                            )
                        }
                    },
                    onError = { error ->
                        scope.launch {
                            showErrorSnackbar(
                                snackbarHostState = bottomSheetScaffoldState.snackbarHostState,
                                message = error
                            )
                        }
                    }
                )
            else {
                LoginSheet(
                    viewModel = viewModel,
                    scope = scope,
                    navigateToQuestions = { navigateToQuestions() },
                    patientIds = patientIds,
                    isLoadingState = isLoadingState,
                    onRegister = { viewModel.registerMode = true },
                    onError = { error ->
                        scope.launch {
                            showErrorSnackbar(
                                snackbarHostState = bottomSheetScaffoldState.snackbarHostState,
                                message = error
                            )
                        }
                    }
                )
            }
        },
        sheetPeekHeight = 0.dp,
        snackbarHost = {
            CustomSnackbarHost(
                snackbarHostState = bottomSheetScaffoldState.snackbarHostState,
                modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
            )
        }
    ) { innerPadding ->
        // main content
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState()),
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
            Text(
                text = "Designed by Lee Hao Yang (34393862)",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            )
        }
    }
}

@Composable
fun LoginSheet(
    viewModel: LoginViewModel,
    scope: CoroutineScope,
    navigateToQuestions: () -> Unit,
    patientIds: List<String>,
    isLoadingState: LoginScreenState,
    onRegister: () -> Unit,
    onError: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState()),
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
            expanded = viewModel.loginSheetDropdownExpanded,
            onExpandedChange = { viewModel.loginSheetDropdownExpanded = it },
            value = viewModel.loginUserId,
            onValueChange = { viewModel.loginUserId = it },
        )
        Spacer(modifier = Modifier.size(16.dp))
        CustomPasswordTextField(
            labelText = "Password",
            password = viewModel.loginPassword,
            onPasswordChange = { viewModel.loginPassword = it },
            passwordVisible = viewModel.loginPasswordVisible,
            onToggleVisiblity = { viewModel.loginPasswordVisible = !viewModel.loginPasswordVisible }
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
                        viewModel.login()
                            .onSuccess { _ ->
                                keyboardController?.hide()
                                navigateToQuestions()
                                viewModel.resetAllStates()
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
    viewModel: LoginViewModel,
    scope: CoroutineScope,
    patientIds: List<String>,
    isLoadingState: LoginScreenState,
    onLogin: () -> Unit,
    onSuccess: suspend (String) -> Unit,
    onError: suspend (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState()),
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
            expanded = viewModel.registerSheetDropdownExpanded,
            onExpandedChange = { viewModel.registerSheetDropdownExpanded = it },
            value = viewModel.registerUserId,
            onValueChange = { viewModel.registerUserId = it },
        )
        Spacer(modifier = Modifier.size(16.dp))
        TextField(
            value = viewModel.registerName,
            onValueChange = { viewModel.registerName = it },
            label = { Text("Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.size(16.dp))
        TextField(
            value = viewModel.registerPhoneNumber,
            onValueChange = { viewModel.registerPhoneNumber = it },
            label = { Text("Phone Number") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.size(16.dp))
        CustomPasswordTextField(
            labelText = "Password",
            password = viewModel.registerPassword,
            onPasswordChange = { viewModel.registerPassword = it },
            passwordVisible = viewModel.registerPasswordVisible,
            onToggleVisiblity = { viewModel.registerPasswordVisible = !viewModel.registerPasswordVisible }
        )
        Spacer(modifier = Modifier.size(16.dp))
        CustomPasswordTextField(
            labelText = "Confirm Password",
            password = viewModel.registerConfirmPassword,
            onPasswordChange = { viewModel.registerConfirmPassword = it },
            passwordVisible = viewModel.registerConfirmPasswordVisible,
            onToggleVisiblity = { viewModel.registerConfirmPasswordVisible = !viewModel.registerConfirmPasswordVisible }
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
                        viewModel.register()
                            .onSuccess { success ->
                                keyboardController?.hide()
                                viewModel.resetRegisterState()
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

