package com.sdss.workout.main

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sdss.workout.R
import com.sdss.workout.intro.IntroScreens

data class BottomNavigationitem(
    val route: String,
    val icon: ImageVector,
    val iconContentDescription: String
)

val bottomNavigationItems = listOf(
    BottomNavigationitem(
        MainScreens.WorkoutScreen.route,
        Icons.Default.Star,
        "Workout"
    ),
    BottomNavigationitem(
        MainScreens.ProgressScreen.route,
        Icons.Filled.Star,
        "Progress"
    ),
    BottomNavigationitem(
        MainScreens.HistoryScreen.route,
        Icons.Filled.Star,
        "History"
    )
)

@Composable
fun MainScreenLayout(navController: NavHostController){
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_menu_24),
                            contentDescription = null
                        )
                    }
                },
                title = { Text(text = "Jetpack Compose") },
            )
        },

        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomNavigationItems.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                screen.icon,
                                contentDescription = screen.iconContentDescription
                            )
                        },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(navController, startDestination = IntroScreens.WelcomeCarousel.route) {
            composable(MainScreens.WorkoutScreen.route) {
                MainScreenLayout(navController = navController)
            }
            composable(MainScreens.ProgressScreen.route) {
                // Screen
            }
            composable(MainScreens.HistoryScreen.route) {
                //screen
            }
        }
    }
}