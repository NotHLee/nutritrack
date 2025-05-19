package com.Lee_34393862.nutritrack.shared

import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

data class BottomBarItem(val route: String, val title: String, val icon: ImageVector? = null, val drawableResId: Int? = null)

@Composable
fun CustomBottomBar(
    navController: NavHostController,
    bottomBarItems: List<BottomBarItem>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val visible by remember(currentRoute, bottomBarItems) { mutableStateOf<Boolean>(
        bottomBarItems.any { bottomBarItem -> bottomBarItem.route == currentRoute}
    ) }

    if (visible) {
        NavigationBar {
            bottomBarItems.forEachIndexed { index, screen ->
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
}
