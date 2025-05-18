package com.Lee_34393862.nutritrack

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.Lee_34393862.nutritrack.data.datasource.CsvDataSource
import com.Lee_34393862.nutritrack.data.repositories.FoodIntakeRepository
import com.Lee_34393862.nutritrack.data.repositories.PatientRepository
import com.Lee_34393862.nutritrack.data.repositories.UserRepository
import com.Lee_34393862.nutritrack.data.viewmodel.LoginViewModel
import com.Lee_34393862.nutritrack.data.viewmodel.QuestionsViewModel
import com.Lee_34393862.nutritrack.ui.Dashboard
import com.Lee_34393862.nutritrack.ui.LoginScreen
import com.Lee_34393862.nutritrack.ui.QuestionScreen
import com.Lee_34393862.nutritrack.ui.theme.NutritrackTheme
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import com.Lee_34393862.nutritrack.data.repositories.FruityViceRepository
import com.Lee_34393862.nutritrack.data.repositories.MessageRepository
import kotlinx.coroutines.flow.collectLatest

sealed class Screens(val route: String) {
    data object Login: Screens("login")
    data object Question: Screens("question")
    data object Dashboard: Screens("dashboard")
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // launch a coroutine to populate db (assuming db is never populated)
        lifecycleScope.launch {
            // AppDatabase.getDatabase(context = this@MainActivity).patientDao().deleteAllPatients()
            CsvDataSource.parseCSV(context = applicationContext)
        }

        enableEdgeToEdge()
        setContent {
            NutritrackTheme {
                val navController: NavHostController = rememberNavController()
                val userRepository = UserRepository(context = applicationContext)
                val patientRepository = PatientRepository(context = applicationContext)
                val foodIntakeRepository = FoodIntakeRepository(context = applicationContext)
                val messageRepository = MessageRepository(context = applicationContext)

                NavHost(
                    navController = navController,
                    startDestination = Screens.Login.route
                ) {
                    composable(Screens.Login.route) {
                        LoginScreen(
                            navController = navController,
                            viewModel = LoginViewModel(
                                patientRepository = patientRepository,
                                userRepository = userRepository
                            )

                        )
                    }
                    composable(Screens.Question.route) {
                        QuestionScreen(
                            navController = navController,
                            viewModel = QuestionsViewModel(
                                foodIntakeRepository = foodIntakeRepository,
                                userRepository = userRepository
                            )
                        )
                    }
                    composable(Screens.Dashboard.route) {
                        Dashboard(
                            userRepository = userRepository,
                            messageRepository = messageRepository,
                            navigateToQuestion = { navController.navigate(Screens.Question.route) },
                            navigateToLogin = { navController.navigate(Screens.Login.route) {
                                // clear back stack up to login page
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                                // avoid multiple copies of the same destination
                                launchSingleTop = true
                            } }
                        )
                    }
                }
            }
        }
    }
}


