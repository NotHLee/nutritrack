package com.Lee_34393862.nutritrack.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.Lee_34393862.nutritrack.R
import com.Lee_34393862.nutritrack.Screens
import com.Lee_34393862.nutritrack.data.viewmodel.QuestionsViewModel
import com.Lee_34393862.nutritrack.shared.CustomDropdownSelector
import com.Lee_34393862.nutritrack.shared.CustomTimePicker
import java.time.LocalTime

data class Persona(val name: String, val description: String, val picture: Int, var isExpanded: Boolean = false)
data class Food(val name: String, var checked: Boolean = false)
data class TimeBox(val question: String, var time: LocalTime = LocalTime.MIDNIGHT, var isOpen: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    navController: NavHostController,
    viewModel: QuestionsViewModel,
) {

    val context: Context = LocalContext.current

    val foodIntakeResponses by viewModel.foodIntakeResponses.collectAsState()
    val foodList = remember(foodIntakeResponses) {
        mutableStateListOf<Food>(
            Food(name = "Fruits", checked = foodIntakeResponses?.fruits == true),
            Food(name = "Red Meat", checked = foodIntakeResponses?.redMeat == true),
            Food(name = "Fish", checked = foodIntakeResponses?.fish == true),
            Food(name = "Vegetables", checked = foodIntakeResponses?.vegetables == true),
            Food(name = "Seafood", checked = foodIntakeResponses?.seafood == true),
            Food(name = "Eggs", checked = foodIntakeResponses?.eggs == true),
            Food(name = "Grains", checked = foodIntakeResponses?.grains == true),
            Food(name = "Poultry", checked = foodIntakeResponses?.poultry == true),
            Food(name = "Nuts/Seeds", checked = foodIntakeResponses?.nutsOrSeeds == true)
        )
    }
    val personaList = remember(foodIntakeResponses) {
        mutableStateListOf<Persona>(
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
    }
    var selectedPersona by remember(foodIntakeResponses) { mutableStateOf<Persona?>(personaList.find { persona -> persona.name == foodIntakeResponses?.persona })}
    var personaDropdownExpanded by remember { mutableStateOf<Boolean>(false) }
    val timeBoxList = remember(foodIntakeResponses) {
        mutableStateListOf<TimeBox>(
            TimeBox(
                question = "What time of day approx, do you normally eat your biggest meal?",
                time = LocalTime.parse(foodIntakeResponses?.biggestMealTime ?: "00:00")
            ),
            TimeBox(
                question = "What time of day approx, do you go to sleep at night?",
                time = LocalTime.parse(foodIntakeResponses?.sleepTime ?: "00:00")
            ),
            TimeBox(
                question = "What time of day approx, do you wake up in the morning?",
                time = LocalTime.parse(foodIntakeResponses?.sleepTime ?: "00:00")
            )
        )
    }


    if (viewModel.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Food Intake Questionnaire",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.navigate(Screens.Login.route)
                            }
                        )
                        {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "back button"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                FoodCategories(
                    foodList = foodList,
                    onCheckedChange = { index, checked ->
                        foodList[index] =
                            foodList[index].copy(checked = checked)
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                PersonaInfo(
                    personaList = personaList,
                    onExpand = { index ->
                        personaList[index] =
                            personaList[index].copy(isExpanded = true)
                    },
                    onDismissRequest = { index ->
                        personaList[index] =
                            personaList[index].copy(isExpanded = false)
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Text(
                    "Which persona best fits you?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                CustomDropdownSelector(
                    items = personaList.map { persona -> persona.name },
                    label = "Select persona",
                    value = selectedPersona?.name ?: "",
                    onValueChange = { name ->
                        selectedPersona =
                            personaList.find { persona -> persona.name == name }
                    },
                    expanded = personaDropdownExpanded,
                    onExpandedChange = { personaDropdownExpanded = it }
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                TimeQuestions(
                    context = context,
                    items = timeBoxList,
                    onTimeChange = { index, time ->
                        timeBoxList[index] =
                            timeBoxList[index].copy(time = time)
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(top = 4.dp, bottom = 8.dp))
                Button(
                    onClick = {
                        navController.navigate("dashboard")
                        viewModel.savePreference(
                            fruits = foodList[0].checked,
                            redMeat = foodList[1].checked,
                            fish = foodList[2].checked,
                            vegetables = foodList[3].checked,
                            seafood = foodList[4].checked,
                            eggs = foodList[5].checked,
                            grains = foodList[6].checked,
                            poultry = foodList[7].checked,
                            nutsOrSeeds = foodList[8].checked,
                            persona = selectedPersona?.name ?: "",
                            biggestMealTime = timeBoxList[0].time,
                            sleepTime = timeBoxList[1].time,
                            wakeUpTime = timeBoxList[2].time                        )
                    },
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.contentColorFor(
                            MaterialTheme.colorScheme.tertiaryContainer
                        ),
                        disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        disabledContentColor = MaterialTheme.colorScheme.contentColorFor(
                            MaterialTheme.colorScheme.tertiaryContainer
                        )
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(0.45f),
                    shape = RoundedCornerShape(size = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(painterResource(R.drawable.save), contentDescription = "save icon")
                        Spacer(modifier = Modifier.size(4.dp))
                        Text(
                            "Save",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun FoodCategories(
    foodList: List<Food>,
    onCheckedChange: (Int, Boolean) -> Unit
) {
    Text(
        "Tick all the food categories you can eat",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        itemsIndexed(foodList) { index, food ->
            Row {
                Checkbox(
                    checked = food.checked,
                    onCheckedChange = { onCheckedChange(index, !food.checked) }
                )
                Text(
                    food.name,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun PersonaInfo(
    personaList: List<Persona>,
    onExpand: (Int) -> Unit,
    onDismissRequest: (Int) -> Unit,
) {

    Text(
        "Your Persona",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
    Spacer(modifier = Modifier.size(8.dp))
    Text(
        "People can be broadly classified into 6 different types based on their eating preferences. " +
        "Click on each button below to find out the different types, and select the type that best fits you!",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
    LazyVerticalGrid (
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        itemsIndexed(personaList) { index, persona ->
            PersonaDialog(
                name = persona.name,
                description = persona.description,
                picture = persona.picture,
                isExpanded = persona.isExpanded,
                onExpand = { onExpand(index) },
                onDismissRequest = { onDismissRequest(index) }
            )
        }
    }
}

@Composable
fun PersonaDialog(
    name: String,
    description: String,
    picture: Int,
    isExpanded: Boolean,
    onExpand: () -> Unit,
    onDismissRequest: () -> Unit
) {
    TextButton(
        onClick = { onExpand() },
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.primaryContainer),
            disabledContentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.secondaryContainer),
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Text(
            name,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }

    if (isExpanded) {
        Dialog(
            onDismissRequest = { onDismissRequest() }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    painter = painterResource(picture),
                    contentDescription = "persona image",
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Button(
                    onClick = { onDismissRequest() },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 8.dp)
                ) {
                    Text("Dismiss")
                }
            }
        }
    }
}

@Composable
fun TimeQuestions(
    context: Context,
    items: List<TimeBox>,
    onTimeChange: (Int, LocalTime) -> Unit,
) {
    Column (
        modifier = Modifier.padding(horizontal = 16.dp),
    ){
        Text(
            "Timings",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.size(size = 8.dp))
        items.forEachIndexed { index, item ->
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    item.question,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(0.7f)
                )
                CustomTimePicker(
                    context = context,
                    time = item.time,
                    onTimeChange = { time -> onTimeChange(index, time) }
                )
            }
        }
    }
}
