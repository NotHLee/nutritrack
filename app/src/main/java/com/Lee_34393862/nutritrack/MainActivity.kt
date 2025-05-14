package com.Lee_34393862.nutritrack

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.Lee_34393862.nutritrack.data.AppDatabase
import com.Lee_34393862.nutritrack.data.PatientRepository
import com.Lee_34393862.nutritrack.data.datasource.CsvDataSource
import com.Lee_34393862.nutritrack.screen.Dashboard
import com.Lee_34393862.nutritrack.screen.LoginScreen
import com.Lee_34393862.nutritrack.screen.QuestionScreen
import com.Lee_34393862.nutritrack.ui.theme.NutritrackTheme
import kotlinx.coroutines.flow.first
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
            AppDatabase.getDatabase(context = this@MainActivity).patientDao().deleteAllPatients()
            AppDatabase.getDatabase(context = this@MainActivity).foodIntakeDao().deleteAllFoodIntake()
            CsvDataSource.parseCSV(context = this@MainActivity)
            Log.d("1", AppDatabase.getDatabase(context = this@MainActivity).patientDao().getAllPatients().first().toString())
            Log.d("2", AppDatabase.getDatabase(context = this@MainActivity).foodIntakeDao().getAllFoodIntake().first().toString())

        }

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
                        Dashboard(
                            patientRepository = patientRepository,
                            navigateToQuestion = { navController.navigate(Screens.Question.route) }
                        )
                    }
                }
            }
        }
    }
}


