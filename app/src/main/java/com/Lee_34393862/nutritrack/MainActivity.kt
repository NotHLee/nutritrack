package com.Lee_34393862.nutritrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.Lee_34393862.nutritrack.data.datasource.CsvDataSource
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
                val loginViewModel: LoginViewModel = ViewModelProvider(
                    this, LoginViewModel.LoginViewModelFactory(this@MainActivity)
                )[LoginViewModel::class.java]
                val questionsViewModel: QuestionsViewModel = ViewModelProvider(
                    this, QuestionsViewModel.QuestionsViewModelFactory(this@MainActivity)
                )[QuestionsViewModel::class.java]
                val homeViewModel: HomeViewModel = ViewModelProvider(
                    this, HomeViewModel.HomeViewModelFactory()
                )[HomeViewModel::class.java]
                val insightsViewModel: InsightsViewModel = ViewModelProvider(
                    this, InsightsViewModel.InsightsViewModelFactory(this@MainActivity)
                )[InsightsViewModel::class.java]
                val nutritrackViewModel: NutritrackViewModel = ViewModelProvider(
                    this, NutritrackViewModel.NutritrackViewModelFactory(this@MainActivity)
                )[NutritrackViewModel::class.java]
                val settingsViewModel: SettingsViewModel = ViewModelProvider(
                    this, SettingsViewModel.SettingsViewModelFactory(this@MainActivity)
                )[SettingsViewModel::class.java]
                val clinicianViewModel: ClinicianViewModel = ViewModelProvider(
                    this, ClinicianViewModel.ClinicianViewModelFactory(this@MainActivity)
                )[ClinicianViewModel::class.java]

                NavigationManager(
                    loginViewModel = loginViewModel,
                    questionsViewModel = questionsViewModel,
                    homeViewModel = homeViewModel,
                    insightsViewModel = insightsViewModel,
                    nutritrackViewModel = nutritrackViewModel,
                    settingsViewModel = settingsViewModel,
                    clinicianViewModel = clinicianViewModel,
                )
            }
        }
    }
}


