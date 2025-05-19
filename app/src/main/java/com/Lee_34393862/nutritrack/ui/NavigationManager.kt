package com.Lee_34393862.nutritrack.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.Lee_34393862.nutritrack.data.repositories.FoodIntakeRepository
import com.Lee_34393862.nutritrack.data.repositories.MessageRepository
import com.Lee_34393862.nutritrack.data.repositories.PatientRepository
import com.Lee_34393862.nutritrack.data.repositories.UserRepository
import com.Lee_34393862.nutritrack.data.viewmodel.ClinicianViewModel
import com.Lee_34393862.nutritrack.data.viewmodel.LoginViewModel
import com.Lee_34393862.nutritrack.data.viewmodel.QuestionsViewModel

/**
 * This nested navigation manager is following the official documentation way of doing it
 * Advantage of doing it this way is for type safe navigation
 * Source: https://developer.android.com/guide/navigation/design/nested-graphs
 */

// Top-level screens
sealed class Screens(val route: String) {
    object Login: DashboardScreens(route = "login")
    object Questions: DashboardScreens(route = "questions")
    object Dashboard: DashboardScreens(route = "dashboard")
    object Clinician: DashboardScreens(route = "clinician")
}

@Composable
fun NavigationManager(
    userRepository: UserRepository,
    patientRepository: PatientRepository,
    foodIntakeRepository: FoodIntakeRepository,
    messageRepository: MessageRepository
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
                viewModel = LoginViewModel(
                    patientRepository = patientRepository,
                    userRepository = userRepository
                ),
                navigateToQuestions = { navController.navigate(Screens.Dashboard.route) }
            )
        }
        composable(route = Screens.Questions.route) {
            QuestionScreen(
                navigateToLogin = { navController.navigate(Screens.Login.route) },
                navigateToDashboard = { navController.navigate(Screens.Dashboard.route) },
                viewModel = QuestionsViewModel(
                    foodIntakeRepository = foodIntakeRepository,
                    userRepository = userRepository
                )
            )
        }
        composable(route = Screens.Dashboard.route) {
            Dashboard(
                userRepository = userRepository,
                messageRepository = messageRepository,
                navigateToQuestion = { navController.navigate(Screens.Questions.route) },
                navigateToClinician = { navController.navigate(Screens.Clinician.route) }
            )
        }
        composable(route = Screens.Clinician.route) {
            ClinicianScreen(
                viewModel = ClinicianViewModel(

                )
            )
        }
    }
}