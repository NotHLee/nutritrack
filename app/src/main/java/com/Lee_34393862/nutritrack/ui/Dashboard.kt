package com.Lee_34393862.nutritrack.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.Lee_34393862.nutritrack.R
import com.Lee_34393862.nutritrack.data.repositories.FruityViceRepository
import com.Lee_34393862.nutritrack.data.repositories.MessageRepository
import com.Lee_34393862.nutritrack.data.repositories.UserRepository
import com.Lee_34393862.nutritrack.data.viewmodel.HomeViewModel
import com.Lee_34393862.nutritrack.data.viewmodel.InsightsViewModel
import com.Lee_34393862.nutritrack.data.viewmodel.NutritrackViewModel
import com.Lee_34393862.nutritrack.data.viewmodel.SettingsViewModel
import com.Lee_34393862.nutritrack.screen.HomeScreen
import com.Lee_34393862.nutritrack.shared.BottomBarItem
import com.Lee_34393862.nutritrack.shared.CustomBottomBar
import com.Lee_34393862.nutritrack.shared.CustomSnackbarHost

// screens inside Dashboard
sealed class DashboardScreens(val route: String) {
    object Home: DashboardScreens(route = "home")
    object Insights: DashboardScreens(route = "insights")
    object Nutritrack: DashboardScreens(route = "nutritrack")
    object Settings: DashboardScreens(route = "settings")
}

@Composable
fun Dashboard(
    userRepository: UserRepository,
    messageRepository: MessageRepository,
    navigateToQuestion: () -> Unit,
    navigateToClinician: () -> Unit,
) {

    val scope = rememberCoroutineScope()
    val navController: NavHostController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val bottomBarItems: List<BottomBarItem> = listOf(
        BottomBarItem(
            route = DashboardScreens.Home.route,
            title = "Home",
            icon = Icons.Default.Home
        ),
        BottomBarItem(
            route = DashboardScreens.Insights.route,
            title = "Insights",
            drawableResId = R.drawable.insights
        ),
        BottomBarItem(
            route = DashboardScreens.Nutritrack.route,
            title = "Nutritrack",
            drawableResId = R.drawable.nutricoach
        ),
        BottomBarItem(
            route = DashboardScreens.Settings.route,
            title = "Settings",
            icon = Icons.Default.Settings
        )
    )

    // pre load FruityViceRepository to cache fruit suggestions upon login
    val fruityViceRepository = FruityViceRepository(scope = scope)

    Scaffold (
        snackbarHost = { CustomSnackbarHost(snackbarHostState = snackbarHostState) },
        bottomBar = { CustomBottomBar(navController = navController, bottomBarItems = bottomBarItems) },
    ){ innerPadding ->
        NavHost(
            navController = navController,
            startDestination = DashboardScreens.Home.route
        ) {
            composable(route = DashboardScreens.Home.route) {
                HomeScreen(
                    innerPadding,
                    viewModel = HomeViewModel(userRepository = userRepository),
                    navigateToQuestion = { navigateToQuestion() },
                    navigateToInsights = { navController.navigate(DashboardScreens.Insights.route) }
                )
            }
            composable(route = DashboardScreens.Insights.route) {
                InsightsScreen(
                    innerPadding,
                    viewModel = InsightsViewModel(userRepository = userRepository),
                    navigateToNutritrack = { navController.navigate(DashboardScreens.Nutritrack.route) }
                )
            }
            composable(route = DashboardScreens.Nutritrack.route) {
                NutritrackScreen(
                    innerPadding,
                    viewModel = NutritrackViewModel(
                        userRepository = userRepository,
                        fruityViceRepository = fruityViceRepository,
                        messageRepository = messageRepository
                    )
                )
            }
            composable(route = DashboardScreens.Settings.route) {
                SettingsScreen(
                    innerPadding,
                    snackbarHostState = snackbarHostState,
                    navigateToClinician = { navigateToClinician() },
                    viewModel = SettingsViewModel(userRepository = userRepository)
                )
            }
        }
    }
}

