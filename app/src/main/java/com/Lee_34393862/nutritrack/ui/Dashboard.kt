package com.Lee_34393862.nutritrack.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.Lee_34393862.nutritrack.R
import com.Lee_34393862.nutritrack.Screens
import com.Lee_34393862.nutritrack.data.PatientRepository
import com.Lee_34393862.nutritrack.data.repositories.UserRepository
import com.Lee_34393862.nutritrack.data.viewmodel.HomeViewModel
import com.Lee_34393862.nutritrack.screen.HomeScreen

sealed class DashboardScreens(
    val route: String,
    val title: String,
    val icon: ImageVector? = null,
    val drawableResId: Int? = null
) {
    object Home: DashboardScreens(
        route = "dashboard/home",
        title = "Home",
        icon = Icons.Default.Home
    )
    object Insights: DashboardScreens(
        route = "dashboard/insights",
        title = "Insights",
        drawableResId = R.drawable.insights
    )
    object Nutritrack: DashboardScreens(
        route = "dashboard/nutritrack",
        title = "Nutritrack",
        drawableResId = R.drawable.nutricoach
    )
    object Settings: DashboardScreens(
        route = "dashboard/settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )
}

@Composable
fun Dashboard(
    userRepository: UserRepository,
    navigateToQuestion: () -> Unit
) {

    val navController: NavHostController = rememberNavController()

    Scaffold (
        bottomBar = { DashboardBottomBar(navController) }
    ){ innerPadding ->
        NavHost(
            navController = navController,
            route = Screens.Dashboard.route,
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
//            composable(route = DashboardScreens.Insights.route) {
//                InsightsScreen(
//                    innerPadding,
//                    patientRepository = patientRepository,
//                    navigateToNutritrack = { navController.navigate(DashboardScreens.Nutritrack.route) }
//                )
//            }
            composable(route = DashboardScreens.Nutritrack.route) {
                NutritrackScreen(innerPadding)
            }
            composable(route = DashboardScreens.Settings.route) {
                SettingsScreen(innerPadding)
            }
        }
    }
}

@Composable
fun DashboardBottomBar(
    navController: NavHostController,
) {
    val screens: List<DashboardScreens> = listOf(
        DashboardScreens.Home,
        DashboardScreens.Insights,
        DashboardScreens.Nutritrack,
        DashboardScreens.Settings
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState().value

    NavigationBar {
        screens.forEachIndexed { index, screen ->
            NavigationBarItem(
                icon = {
                    if (screen.icon != null) {
                        Icon(
                            screen.icon,
                            contentDescription = screen.title,
                            modifier = Modifier.size(24.dp)
                        )
                    } else if (screen.drawableResId != null) {
                        Icon(
                            painterResource(screen.drawableResId),
                            contentDescription = screen.title,
                            modifier = Modifier.size(24.dp)
                        )
                    } else (
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = "error",
                            modifier = Modifier.size(24.dp)
                        )
                    )
                },
                label = { Text(screen.title) },
                onClick = {
                    navController.navigate(screen.route)
                },
                selected = navBackStackEntry?.destination?.route == screen.route,
            )
        }
    }
}