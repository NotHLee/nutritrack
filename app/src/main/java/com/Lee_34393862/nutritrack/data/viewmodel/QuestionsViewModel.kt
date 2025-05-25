package com.Lee_34393862.nutritrack.data.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.Lee_34393862.nutritrack.R
import com.Lee_34393862.nutritrack.data.AuthManager
import com.Lee_34393862.nutritrack.data.entities.FoodIntake
import com.Lee_34393862.nutritrack.data.repositories.FoodIntakeRepository
import com.Lee_34393862.nutritrack.ui.Food
import com.Lee_34393862.nutritrack.ui.Persona
import com.Lee_34393862.nutritrack.ui.TimeBox
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
    val foodList = mutableStateListOf<Food>(
        Food(name = "Fruits", checked = false),
        Food(name = "Red Meat", checked = false),
        Food(name = "Fish", checked = false),
        Food(name = "Vegetables", checked = false),
        Food(name = "Seafood", checked = false),
        Food(name = "Eggs", checked = false),
        Food(name = "Grains", checked = false),
        Food(name = "Poultry", checked = false),
        Food(name = "Nuts/Seeds", checked = false)
    )
    val personaList = mutableStateListOf<Persona>(
        Persona(
            name = "Health Devotee",
            description = "I'm passionate about healthy eating & health plays a big part in my life. I use social media to follow active lifestyle personalities or get new recipes/exercise ideas. I may even buy superfoods or follow a particular type of diet. I like to think I am super healthy.",
            picture = R.drawable.persona_1,
        ),
        Persona(
            name = "Mindful Eater",
            description = "I'm health-conscious and being healthy and eating healthy is important to me. Although health means different things to different people, I make conscious lifestyle decisions about eating based on what I believe healthy means. I look for new recipes and healthy eating information on social media.",
            picture = R.drawable.persona_2,
        ),
        Persona(
            name = "Wellness Striver",
            description = "I aspire to be healthy (but struggle sometimes). Healthy eating is hard work! I've tried to improve my diet, but always find things that make it difficult to stick with the changes. Sometimes I notice recipe ideas or healthy eating hacks, and if it seems easy enough, I'll give it a go.",
            picture = R.drawable.persona_3,
        ),
        Persona(
            name = "Balance Seeker",
            description = "I try and live a balanced lifestyle, and I think that all foods are okay in moderation. I shouldn't have to feel guilty about eating a piece of cake now and again. I get all sorts of inspiration from social media like finding out about new restaurants, fun recipes and sometimes healthy eating tips.",
            picture = R.drawable.persona_4,
        ),
        Persona(
            name = "Health Procrastinator",
            description = "I'm contemplating healthy eating but it's not a priority for me right now. I know the basics about what it means to be healthy, but it doesn't seem relevant to me right now. I have taken a few steps to be healthier but I am not motivated to make it a high priority because I have too many other things going on in my life.",
            picture = R.drawable.persona_5,
        ),
        Persona(
            name = "Food Carefree",
            description = "I'm not bothered about healthy eating. I don't really see the point and I don't think about it. I don't really notice healthy eating tips or recipes and I don't care what I eat.",
            picture = R.drawable.persona_6,
        )
    )
    var selectedPersonaName by mutableStateOf<String>(personaList.find { persona -> persona.name == foodIntakeResponses.value?.persona }?.name ?: "")
    val timeBoxList = mutableStateListOf<TimeBox>(
        TimeBox(
            question = "What time of day approx, do you normally eat your biggest meal?",
            time = LocalTime.parse("00:00")
        ),
        TimeBox(
            question = "What time of day approx, do you go to sleep at night?",
            time = LocalTime.parse("00:00")
        ),
        TimeBox(
            question = "What time of day approx, do you wake up in the morning?",
            time = LocalTime.parse("00:00")
        )
    )

    fun reset() {
        foodList.forEach { it.checked = false }
        selectedPersonaName = ""
        timeBoxList.forEach { it.time = LocalTime.parse("00:00") }
    }

    // initial loading of values
    init {
        viewModelScope.launch {
            // use collectLatest to ensure we get the newest current user
            AuthManager.currentUser.collectLatest { user ->
                if (user != null) {
                    // load food intake data once user exist
                    foodIntakeRepository.getFoodIntakeByUserId(user.userId).collect { foodIntake ->
                        foodList[0].checked = foodIntake?.fruits == true
                        foodList[1].checked = foodIntake?.redMeat == true
                        foodList[2].checked = foodIntake?.fish == true
                        foodList[3].checked = foodIntake?.vegetables == true
                        foodList[4].checked = foodIntake?.seafood == true
                        foodList[5].checked = foodIntake?.eggs == true
                        foodList[6].checked = foodIntake?.grains == true
                        foodList[7].checked = foodIntake?.poultry == true
                        foodList[8].checked = foodIntake?.nutsOrSeeds == true
                        selectedPersonaName = foodIntake?.persona ?: ""
                        timeBoxList[0].time = LocalTime.parse(foodIntake?.biggestMealTime ?: "00:00")
                        timeBoxList[1].time = LocalTime.parse(foodIntake?.sleepTime ?: "00:00")
                        timeBoxList[2].time = LocalTime.parse(foodIntake?.wakeUpTime ?: "00:00")
                    }
                } else {
                    reset()
                }
            }
        }
    }

    // save food intake questionnaire to db
    fun savePreference() : Result<String> {

        // validation: atleast one food item must be selected
        if (foodList.none { it.checked }) {
            return Result.failure(Exception("Please select at least one food item"))
        }

        // validation: check if persona is selected
        if (selectedPersonaName.isEmpty()) {
            return Result.failure(Exception("Please select a persona"))
        }

        // validation: check if any of the times are same
        val times = listOf(
            timeBoxList[0].time,
            timeBoxList[1].time,
            timeBoxList[2].time
        )
        if (times.toSet().size < times.size) {
            // a set cannot have identical instances, so if size of set is lesser than original list
            // that means we have some identical time instances
            return Result.failure(Exception("Timings cannot be identical to each other"))
        }

        viewModelScope.launch {
            foodIntakeRepository.update(
                FoodIntake(
                    userId = AuthManager.currentUser.firstOrNull()?.userId ?: "",
                    fruits = foodList[0].checked,
                    redMeat = foodList[1].checked,
                    fish = foodList[2].checked,
                    vegetables = foodList[3].checked,
                    seafood = foodList[4].checked,
                    eggs = foodList[5].checked,
                    grains = foodList[6].checked,
                    poultry = foodList[7].checked,
                    nutsOrSeeds = foodList[8].checked,
                    persona = selectedPersonaName,
                    biggestMealTime = timeBoxList[0].time.toString(),
                    sleepTime = timeBoxList[1].time.toString(),
                    wakeUpTime = timeBoxList[2].time.toString()
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