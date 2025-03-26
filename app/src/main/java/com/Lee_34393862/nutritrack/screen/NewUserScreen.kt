package com.Lee_34393862.nutritrack.screen

import android.app.Dialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.Lee_34393862.nutritrack.R

data class Persona(val name: String, val description: String, val picture: Int, var isExpanded: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewUserScreen(navController: NavHostController) {

    val checkboxes = remember {
        mutableStateListOf<Boolean>(false,false,false,false,false,false,false,false,false)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(

                title = {
                    Text(
                    "Food Intake Questionnaire",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold
                ) },
                actions = { IconButton(
                    onClick = {
                        navController.popBackStack("question", true)
                        navController.navigate("login")
                    }
                )
                    {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back button")
                    }
                }
            )
        }

    ){ innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ){
            HorizontalDivider()
            FoodCategories(
                checkboxes = checkboxes,
                onCheckedChange = { index, value -> checkboxes[index] = value }
            )
            HorizontalDivider()
            PersonaInfo()
        }



    }
}

@Composable
fun FoodCategories(
    checkboxes: List<Boolean>,
    onCheckedChange: (Int, Boolean) -> Unit
) {
    Spacer(modifier = Modifier.size(8.dp))
    Text(
        "Tick all the food categories you can eat",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
    Spacer(modifier = Modifier.size(8.dp))
    LazyHorizontalGrid(
        rows = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxHeight(0.2f)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        itemsIndexed(checkboxes) { index, checked ->
            Row {
                Checkbox(
                    checked = checked,
                    onCheckedChange = { onCheckedChange(index, !checked) }
                )
            }
        }
    }
}

@Composable
fun PersonaInfo() {

    val personaList = remember { mutableStateListOf(
        Persona(
            name = "Health Devotee",
            description = "I'm passionate about healthy eating & health plays a big part in my life. I use social media to follow active lifestyle personalities or get new recipes/exercise ideas. I may even buy superfoods or follow a particular type of diet. I like to think I am super healthy.",
            picture = R.drawable.persona_1,
            isExpanded = false
        ),
        Persona(
            name = "Mindful Eater",
            description = "I'm health-conscious and being healthy and eating healthy is important to me. Although health means different things to different people, I make conscious lifestyle decisions about eating based on what I believe healthy means. I look for new recipes and healthy eating information on social media.",
            picture = R.drawable.persona_2,
            isExpanded = false
        ),
        Persona(
            name = "Wellness Striver",
            description = "I aspire to be healthy (but struggle sometimes). Healthy eating is hard work! I've tried to improve my diet, but always find things that make it difficult to stick with the changes. Sometimes I notice recipe ideas or healthy eating hacks, and if it seems easy enough, I'll give it a go.",
            picture = R.drawable.persona_3,
            isExpanded = false
        ),
        Persona(
            name = "Balance Seeker",
            description = "I try and live a balanced lifestyle, and I think that all foods are okay in moderation. I shouldn't have to feel guilty about eating a piece of cake now and again. I get all sorts of inspiration from social media like finding out about new restaurants, fun recipes and sometimes healthy eating tips.",
            picture = R.drawable.persona_4,
            isExpanded = false
        ),
        Persona(
            name = "Health Procrastinator",
            description = "I'm contemplating healthy eating but it's not a priority for me right now. I know the basics about what it means to be healthy, but it doesn't seem relevant to me right now. I have taken a few steps to be healthier but I am not motivated to make it a high priority because I have too many other things going on in my life.",
            picture = R.drawable.persona_5,
            isExpanded = false
        ),
        Persona(
            name = "Food Carefree",
            description = "I'm not bothered about healthy eating. I don't really see the point and I don't think about it. I don't really notice healthy eating tips or recipes and I don't care what I eat.",
            picture = R.drawable.persona_6,
            isExpanded = false
        )
    )}

    Spacer(modifier = Modifier.size(8.dp))
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
    LazyHorizontalGrid(
        rows = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxHeight(0.5f)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        itemsIndexed(personaList) { index, persona ->
            PersonaDialog(
                name = persona.name,
                description = persona.description,
                picture = persona.picture,
                isExpanded = persona.isExpanded,
                onExpand = { personaList[index] = personaList[index].copy(isExpanded = true) },
                onDismissRequest = { personaList[index] = personaList[index].copy(isExpanded = false) }
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
    ) {
        Text(
            name,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
        )
    }

        if (isExpanded) {
            Dialog(
                onDismissRequest = { onDismissRequest() }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
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
                    Spacer(modifier = Modifier.size(8.dp))
                }
            }
        }
    }


