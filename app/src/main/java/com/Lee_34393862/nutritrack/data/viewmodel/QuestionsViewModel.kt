package com.Lee_34393862.nutritrack.data.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.Lee_34393862.nutritrack.data.entities.FoodIntake
import com.Lee_34393862.nutritrack.data.repositories.FoodIntakeRepository
import com.Lee_34393862.nutritrack.data.repositories.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.time.LocalTime

class QuestionsViewModel(context: Context, private val userRepository: UserRepository): ViewModel() {

    // TODO: add validation for food intake questionnaire
    private val foodIntakeRepository = FoodIntakeRepository(context = context)

    // store food intake questionnaire responses as flow such that it observes db for changes
    private var _foodIntakeResponses = MutableStateFlow<FoodIntake?>(null)
    val foodIntakeResponses: StateFlow<FoodIntake?> get() = _foodIntakeResponses.asStateFlow()

    var isLoading by mutableStateOf<Boolean>(true)

    // initial loading of values
    init {
        viewModelScope.launch {
            userRepository.currentUser.collect { user ->
                Log.d("user", user?.userId ?: "wtf null")
                if (user != null) {
                    // load food intake data once user exist
                    foodIntakeRepository.getFoodIntakeByUserId(user.userId).collect { foodIntake ->
                        _foodIntakeResponses.value = foodIntake
                    }
                }
            }
        }
        isLoading = false
    }

    // save food intake questionnaire to db
    fun savePreference(
       fruits: Boolean,
       redMeat: Boolean,
       fish: Boolean,
       vegetables: Boolean,
       seafood: Boolean,
       eggs: Boolean,
       grains: Boolean,
       poultry: Boolean,
       nutsOrSeeds: Boolean,
       persona: String,
       biggestMealTime: LocalTime,
       sleepTime: LocalTime,
       wakeUpTime: LocalTime,
    ) {
        viewModelScope.launch {
            foodIntakeRepository.update(
                FoodIntake(
                    userId = userRepository.currentUser.first()?.userId ?: "",
                    fruits = fruits,
                    redMeat = redMeat,
                    fish = fish,
                    vegetables = vegetables,
                    seafood = seafood,
                    eggs = eggs,
                    grains = grains,
                    poultry = poultry,
                    nutsOrSeeds = nutsOrSeeds,
                    persona = persona,
                    biggestMealTime = biggestMealTime.toString(),
                    sleepTime = sleepTime.toString(),
                    wakeUpTime = wakeUpTime.toString(),
                )
            )
        }
    }

    class QuestionsViewModelFactory(context: Context, private val userRepository: UserRepository) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return QuestionsViewModel(context, userRepository) as T
        }
    }

}