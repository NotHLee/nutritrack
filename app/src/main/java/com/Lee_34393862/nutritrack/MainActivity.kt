package com.Lee_34393862.nutritrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.Lee_34393862.nutritrack.data.datasource.CsvDataSource
import com.Lee_34393862.nutritrack.data.repositories.FoodIntakeRepository
import com.Lee_34393862.nutritrack.data.repositories.MessageRepository
import com.Lee_34393862.nutritrack.data.repositories.PatientRepository
import com.Lee_34393862.nutritrack.data.repositories.UserRepository
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
                val userRepository = UserRepository(context = applicationContext)
                val patientRepository = PatientRepository(context = applicationContext)
                val foodIntakeRepository = FoodIntakeRepository(context = applicationContext)
                val messageRepository = MessageRepository(context = applicationContext)

                NavigationManager(
                    userRepository = userRepository,
                    patientRepository = patientRepository,
                    foodIntakeRepository = foodIntakeRepository,
                    messageRepository = messageRepository
                )
            }
        }
    }
}


