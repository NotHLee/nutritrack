package com.Lee_34393862.nutritrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.Lee_34393862.nutritrack.core.data.PatientRepository
import com.Lee_34393862.nutritrack.core.model.Patient
import com.Lee_34393862.nutritrack.feature.home.HomeScreen
import com.Lee_34393862.nutritrack.feature.login.LoginScreen
import com.Lee_34393862.nutritrack.feature.login.LoginScreenViewModel
import com.Lee_34393862.nutritrack.ui.theme.NutritrackTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutritrackTheme {
                var patient: Patient by remember { mutableStateOf(Patient("", "")) }
                var patientRepository: PatientRepository = PatientRepository(context = this@MainActivity)
                val navController: NavHostController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    composable("login") {
                        LoginScreen(
                            navController = navController,
                            patientRepository = patientRepository,
                            onPatientChange = { newPatient -> patient = newPatient }
                        )
                    }
                    composable("home") {
                        HomeScreen()
                    }
                }
            }
        }
    }
}

