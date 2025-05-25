package com.Lee_34393862.nutritrack.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.Lee_34393862.nutritrack.R
import com.Lee_34393862.nutritrack.data.viewmodel.QuestionsViewModel
import com.Lee_34393862.nutritrack.shared.CustomDropdownSelector
import com.Lee_34393862.nutritrack.shared.CustomSnackbarHost
import com.Lee_34393862.nutritrack.shared.CustomTimePicker
import com.Lee_34393862.nutritrack.shared.showErrorSnackbar
import kotlinx.coroutines.launch
import java.time.LocalTime

data class Persona(val name: String, val description: String, val picture: Int, var isExpanded: Boolean = false)
data class Food(val name: String, var checked: Boolean = false)
data class TimeBox(val question: String, var time: LocalTime = LocalTime.MIDNIGHT, var isOpen: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    navigateToLogin: () -> Unit,
    navigateToDashboard: () -> Unit,
    showSuccessSnackbarInDashboard: (String) -> Unit,
    viewModel: QuestionsViewModel,
) {
    val scope = rememberCoroutineScope()
    val context: Context = LocalContext.current
    var personaDropdownExpanded by rememberSaveable { mutableStateOf<Boolean>(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { CustomSnackbarHost(snackbarHostState = snackbarHostState) },
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
                            navigateToLogin()
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
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(state = rememberScrollState())
        ) {
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            FoodCategories(
                foodList = viewModel.foodList,
                onCheckedChange = { index, checked ->
                    viewModel.foodList[index] =
                        viewModel.foodList[index].copy(checked = checked)
                }
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            PersonaInfo(
                personaList = viewModel.personaList,
                onExpand = { index ->
                    viewModel.personaList[index] =
                        viewModel.personaList[index].copy(isExpanded = true)
                },
                onDismissRequest = { index ->
                    viewModel.personaList[index] =
                        viewModel.personaList[index].copy(isExpanded = false)
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
                items = viewModel.personaList.map { persona -> persona.name },
                label = "Select persona",
                value = viewModel.selectedPersonaName,
                onValueChange = { name ->
                    viewModel.selectedPersonaName =
                        viewModel.personaList.find { persona -> persona.name == name }?.name ?: ""
                },
                expanded = personaDropdownExpanded,
                onExpandedChange = { personaDropdownExpanded = it }
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            TimeQuestions(
                context = context,
                items = viewModel.timeBoxList,
                onTimeChange = { index, time ->
                    viewModel.timeBoxList[index] =
                        viewModel.timeBoxList[index].copy(time = time)
                }
            )
            HorizontalDivider(modifier = Modifier.padding(top = 4.dp, bottom = 8.dp))
            Button(
                onClick = {
                    viewModel.savePreference()
                        .onSuccess { success ->
                            navigateToDashboard()
                            showSuccessSnackbarInDashboard(success)
                        }
                        .onFailure { failure ->
                            scope.launch {
                                showErrorSnackbar(snackbarHostState, failure.message.toString())
                            }
                        }
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


@OptIn(ExperimentalLayoutApi::class)
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
    FlowColumn (
        maxItemsInEachColumn = 3,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 16.dp)
    ) {
        foodList.forEachIndexed { index, food ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxColumnWidth()
            ) {
                Checkbox(
                    checked = food.checked,
                    onCheckedChange = { onCheckedChange(index, !food.checked) }
                )
                Text(
                    food.name,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
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
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
    )
     FlowColumn (
        maxItemsInEachColumn = 2,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        personaList.forEachIndexed { index, persona ->
            Box(modifier = Modifier.fillMaxColumnWidth()) {
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
        modifier = Modifier.fillMaxWidth()
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
