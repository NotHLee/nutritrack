package com.Lee_34393862.nutritrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.Lee_34393862.nutritrack.data.PatientRepository
import com.Lee_34393862.nutritrack.screen.Dashboard
import com.Lee_34393862.nutritrack.screen.LoginScreen
import com.Lee_34393862.nutritrack.screen.QuestionScreen
import com.Lee_34393862.nutritrack.ui.theme.NutritrackTheme

sealed class Screens(val route: String) {
    object Login: Screens("login")
    object Question: Screens("question")
    object Dashboard: Screens("dashboard")
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutritrackTheme {
                val patientRepository = PatientRepository(context = this@MainActivity)
                val navController: NavHostController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screens.Login.route
                ) {
                    composable(Screens.Login.route) {
                        LoginScreen(
                            navController = navController,
                            patientRepository = patientRepository,
                        )
                    }
                    composable(Screens.Question.route) {
                        QuestionScreen(
                            navController = navController,
                            patientRepository = patientRepository
                        )
                    }
                    composable(Screens.Dashboard.route) {
                        Dashboard()
                    }
                }
            }
        }
    }
}


