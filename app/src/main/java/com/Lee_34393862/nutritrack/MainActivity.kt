package com.Lee_34393862.nutritrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.Lee_34393862.nutritrack.data.datasource.CsvDataSource
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
import com.Lee_34393862.nutritrack.ui.NavigationManager
import com.Lee_34393862.nutritrack.ui.theme.NutritrackTheme
import kotlinx.coroutines.launch

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
                // only 1 instance of user repository to act as auth manager and have shared user data across app
                val userRepository = UserRepository(context = applicationContext)

                val loginViewModel: LoginViewModel = ViewModelProvider(
                    this, LoginViewModel.LoginViewModelFactory(this@MainActivity, userRepository)
                )[LoginViewModel::class.java]
                val questionsViewModel: QuestionsViewModel = ViewModelProvider(
                    this, QuestionsViewModel.QuestionsViewModelFactory(this@MainActivity, userRepository)
                )[QuestionsViewModel::class.java]
                val homeViewModel: HomeViewModel = ViewModelProvider(
                    this, HomeViewModel.HomeViewModelFactory(this@MainActivity, userRepository)
                )[HomeViewModel::class.java]
                val insightsViewModel: InsightsViewModel = ViewModelProvider(
                    this, InsightsViewModel.InsightsViewModelFactory(this@MainActivity, userRepository)
                )[InsightsViewModel::class.java]
                val nutritrackViewModel: NutritrackViewModel = ViewModelProvider(
                    this, NutritrackViewModel.NutritrackViewModelFactory(this@MainActivity, userRepository)
                )[NutritrackViewModel::class.java]
                val settingsViewModel: SettingsViewModel = ViewModelProvider(
                    this, SettingsViewModel.SettingsViewModelFactory(this@MainActivity, userRepository)
                )[SettingsViewModel::class.java]
                val clinicianViewModel: ClinicianViewModel = ViewModelProvider(
                    this, ClinicianViewModel.ClinicianViewModelFactory(this@MainActivity, userRepository)
                )[ClinicianViewModel::class.java]

                NavigationManager(
                    loginViewModel = loginViewModel,
                    questionsViewModel = questionsViewModel,
                    homeViewModel = homeViewModel,
                    insightsViewModel = insightsViewModel,
                    nutritrackViewModel = nutritrackViewModel,
                    settingsViewModel = settingsViewModel,
                    clinicianViewModel = clinicianViewModel,
                    userRepository = userRepository,
                )
            }
        }
    }
}


