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
import com.Lee_34393862.nutritrack.screen.HomeScreen
import com.Lee_34393862.nutritrack.screen.LoginScreen
import com.Lee_34393862.nutritrack.screen.NewUserScreen
import com.Lee_34393862.nutritrack.ui.theme.NutritrackTheme

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
                    startDestination = "login"
                ) {
                    composable("login") {
                        LoginScreen(
                            navController = navController,
                            patientRepository = patientRepository,
                        )
                    }
                    composable("home") {
                        HomeScreen()
                    }
                    composable("question") {
                        NewUserScreen(
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

