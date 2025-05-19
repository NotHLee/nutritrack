package com.Lee_34393862.nutritrack.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.Lee_34393862.nutritrack.data.repositories.FoodIntakeRepository
import com.Lee_34393862.nutritrack.data.repositories.FruityViceRepository
import com.Lee_34393862.nutritrack.data.repositories.MessageRepository
import com.Lee_34393862.nutritrack.data.repositories.PatientRepository
import com.Lee_34393862.nutritrack.data.repositories.UserRepository
import com.Lee_34393862.nutritrack.data.viewmodel.ClinicianViewModel
import com.Lee_34393862.nutritrack.data.viewmodel.HomeViewModel
import com.Lee_34393862.nutritrack.data.viewmodel.InsightsViewModel
import com.Lee_34393862.nutritrack.data.viewmodel.LoginViewModel
import com.Lee_34393862.nutritrack.data.viewmodel.NutritrackViewModel
import com.Lee_34393862.nutritrack.data.viewmodel.QuestionsViewModel
import com.Lee_34393862.nutritrack.data.viewmodel.SettingsViewModel

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
    userRepository: UserRepository,
) {
    val navController = rememberNavController()
    val isLogin by userRepository.isLogin.collectAsState()

    // redirect user to login whenever the user's auth status changes
    LaunchedEffect(isLogin) {
        if (!isLogin) {
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

    NavHost(navController = navController, startDestination = Screens.Login.route) {
        composable(route = Screens.Login.route) {
            LoginScreen(
                viewModel = loginViewModel,
                navigateToQuestions = { navController.navigate(Screens.Questions.route) }
            )
        }
        composable(route = Screens.Questions.route) {
            QuestionScreen(
                navigateToLogin = { navController.navigate(Screens.Login.route) },
                navigateToDashboard = { navController.navigate(Screens.Dashboard.route) },
                viewModel = questionsViewModel
            )
        }
        composable(route = Screens.Dashboard.route) {
            Dashboard(
                homeViewModel = homeViewModel,
                insightsViewModel = insightsViewModel,
                nutritrackViewModel = nutritrackViewModel,
                settingsViewModel = settingsViewModel,
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