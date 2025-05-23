package com.Lee_34393862.nutritrack.data.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.Lee_34393862.nutritrack.data.AuthManager
import com.Lee_34393862.nutritrack.data.entities.FoodIntake
import com.Lee_34393862.nutritrack.data.repositories.FoodIntakeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalTime

class QuestionsViewModel(context: Context): ViewModel() {

    // TODO: add validation for food intake questionnaire
    private val foodIntakeRepository = FoodIntakeRepository(context = context)

    // store food intake questionnaire responses as flow such that it observes db for changes
    private var _foodIntakeResponses = MutableStateFlow<FoodIntake?>(null)
    val foodIntakeResponses: StateFlow<FoodIntake?> get() = _foodIntakeResponses.asStateFlow()

    // initial loading of values
    init {
        viewModelScope.launch {
            // use collectLatest to ensure we get the newest current user
            AuthManager.currentUser.collectLatest { user ->
                if (user != null) {
                    // load food intake data once user exist
                    foodIntakeRepository.getFoodIntakeByUserId(user.userId).collect { foodIntake ->
                        _foodIntakeResponses.value = foodIntake
                    }
                } else {
                    // reset value if user is null (logged out)
                    _foodIntakeResponses.value = null
                }
            }
        }
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
    ) : Result<String> {

        // validation: check if any of the times are same
        val times = listOf(biggestMealTime, sleepTime, wakeUpTime)
        if (times.toSet().size < times.size) {
            // a set cannot have identical instances, so if size of set is lesser than original list
            // that means we have some identical time instances
            return Result.failure(Exception("Timings cannot be identical to each other"))
        }

        viewModelScope.launch {
            foodIntakeRepository.update(
                FoodIntake(
                    userId = AuthManager.currentUser.firstOrNull()?.userId ?: "",
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
        return Result.success("Preferences successfully saved")
    }

    class QuestionsViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return QuestionsViewModel(context) as T
        }
    }

}