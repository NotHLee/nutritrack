package com.Lee_34393862.nutritrack.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.Lee_34393862.nutritrack.data.AuthManager
import com.Lee_34393862.nutritrack.data.viewmodel.ClinicianViewModel
import com.Lee_34393862.nutritrack.data.viewmodel.HomeViewModel
import com.Lee_34393862.nutritrack.data.viewmodel.InsightsViewModel
import com.Lee_34393862.nutritrack.data.viewmodel.LoginViewModel
import com.Lee_34393862.nutritrack.data.viewmodel.NutritrackViewModel
import com.Lee_34393862.nutritrack.data.viewmodel.QuestionsViewModel
import com.Lee_34393862.nutritrack.data.viewmodel.SettingsViewModel
import com.Lee_34393862.nutritrack.shared.showSuccessSnackbar
import kotlinx.coroutines.launch

// Top-level screens
sealed class Screens(val route: String) {
    object Login: DashboardScreens(route = "login")
    object Questions: DashboardScreens(route = "questions")
    object Dashboard: DashboardScreens(route = "dashboard")
    object Clinician: DashboardScreens(route = "clinician")
}

@Composable
fun NavigationManager(
    loginViewModel: LoginViewModel,
    questionsViewModel: QuestionsViewModel,
    homeViewModel: HomeViewModel,
    insightsViewModel: InsightsViewModel,
    nutritrackViewModel: NutritrackViewModel,
    settingsViewModel: SettingsViewModel,
    clinicianViewModel: ClinicianViewModel,
) {
    val navController = rememberNavController()
    val currentUser by AuthManager.currentUser.collectAsState()

    // redirect user to login whenever current user session is terminated / logout
    LaunchedEffect(currentUser) {
        if (currentUser == null) {
            navController.navigate(Screens.Login.route) {
                // clear back stack up to login page
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
                // avoid multiple copies of the same destination
                launchSingleTop = true
            }
        }
    }

    val scope = rememberCoroutineScope()
    val dashboardSnackHostState = remember { SnackbarHostState() }

    NavHost(navController = navController, startDestination = Screens.Login.route) {
        composable(route = Screens.Login.route) {
            LoginScreen(
                viewModel = loginViewModel,
                navigateToQuestions = { navController.navigate(Screens.Questions.route) }
            )
        }
        composable(route = Screens.Questions.route) {
            QuestionScreen(
                navigateToLogin = { AuthManager.logout() },
                navigateToDashboard = { navController.navigate(Screens.Dashboard.route) },
                showSuccessSnackbarInDashboard = { success ->
                    scope.launch {
                        showSuccessSnackbar(dashboardSnackHostState, success)
                    }
                },
                viewModel = questionsViewModel
            )
        }
        composable(route = Screens.Dashboard.route) {
            Dashboard(
                homeViewModel = homeViewModel,
                insightsViewModel = insightsViewModel,
                nutritrackViewModel = nutritrackViewModel,
                settingsViewModel = settingsViewModel,
                snackbarHostState = dashboardSnackHostState,
                navigateToQuestion = { navController.navigate(Screens.Questions.route) },
                navigateToClinician = { navController.navigate(Screens.Clinician.route) }
            )
        }
        composable(route = Screens.Clinician.route) {
            ClinicianScreen(
                viewModel = clinicianViewModel,
                navigateBack = { navController.popBackStack() },
            )
        }
    }
}