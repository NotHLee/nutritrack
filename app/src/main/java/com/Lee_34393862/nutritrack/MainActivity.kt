package com.Lee_34393862.nutritrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.Lee_34393862.nutritrack.data.datasource.CsvDataSource
import com.Lee_34393862.nutritrack.data.repositories.PatientRepository
import com.Lee_34393862.nutritrack.data.repositories.UserRepository
import com.Lee_34393862.nutritrack.data.viewmodel.LoginViewModel
import com.Lee_34393862.nutritrack.ui.LoginScreen
import com.Lee_34393862.nutritrack.ui.theme.NutritrackTheme
import kotlinx.coroutines.launch

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
            CsvDataSource.parseCSV(context = this@MainActivity)
        }

        enableEdgeToEdge()
        setContent {
            NutritrackTheme {
                val navController: NavHostController = rememberNavController()
                val userRepository: UserRepository = UserRepository(context = this@MainActivity)
                val patientRepository: PatientRepository = PatientRepository(context = this@MainActivity)

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
//                    composable(Screens.Question.route) {
//                        QuestionScreen(
//                            navController = navController,
//                            patientRepository = patientRepository
//                        )
//                    }
//                    composable(Screens.Dashboard.route) {
//                        Dashboard(
//                            patientRepository = patientRepository,
//                            navigateToQuestion = { navController.navigate(Screens.Question.route) }
//                        )
//                    }
                }
            }
        }
    }
}


