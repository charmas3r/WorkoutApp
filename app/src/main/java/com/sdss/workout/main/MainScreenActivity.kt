package com.sdss.workout.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sdss.workout.R
import com.sdss.workout.base.BaseActivity
import com.sdss.workout.drawer.DrawerItems
import com.sdss.workout.googlesync.GoogleSyncScreens
import com.sdss.workout.googlesync.GoogleSyncSettingsScreen
import com.sdss.workout.googlesync.GoogleSyncSignInScreen
import com.sdss.workout.navigation.shouldShowBackArrowInTopAppBar
import com.sdss.workout.program.*
import com.sdss.workout.settings.*
import com.sdss.workout.ui.drawer.DrawerRow
import com.sdss.workout.ui.styles.headerTextStyle
import com.sdss.workout.ui.theme.WorkoutTheme
import com.sdss.workout.util.shouldShowBottomBarByCurrentRoute
import com.sdss.workout.workout.WorkoutScreen
import kotlinx.coroutines.launch

@ExperimentalPagerApi
class MainScreenActivity : BaseActivity() {
    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProvideWindowInsets {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = MaterialTheme.colors.isLight
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = useDarkIcons
                    )
                }
                MainScreenLayout()
            }
        }
    }
}

data class BottomNavigationitem(
    val route: String,
    val icon: ImageVector,
    val iconContentDescription: String
)

val bottomNavigationItems = listOf(
    BottomNavigationitem(
        DrawerItems.Home.route,
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

val navDrawerItems = DrawerItems.getAllDrawerItems()

@ExperimentalFoundationApi
@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun MainScreenLayout() {
    WorkoutTheme {
        val navController = rememberNavController()
        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
        val scope = rememberCoroutineScope()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        var toState by remember { mutableStateOf(MultiFabState.COLLAPSED) }
        val isBackNavigationAllowed by remember { mutableStateOf(currentDestination?.route?.let {
            shouldShowBackArrowInTopAppBar(
                currentRoute = it
            )
        }) }

        Scaffold(
            topBar = {
                TopAppBar(
                    elevation = if (currentDestination?.route == DrawerItems.WorkoutPrograms.route
                        || currentDestination?.route == ProgramScreens.Overview.route
                        || currentDestination?.route == ProgramScreens.ExerciseSelection.route
                    ) 0.dp else 4.dp,
                    navigationIcon = {
                        IconButton(onClick = {
                            if (isBackNavigationAllowed == true) {
                                navController.popBackStack()
                            } else {
                                scope.launch {
                                    scaffoldState.drawerState.open()
                                }
                            }
                        }) {
                            if (isBackNavigationAllowed == true) {
                                Icon(
                                    painter = if (MaterialTheme.colors.isLight) {
                                        painterResource(id = R.drawable.ic_baseline_arrow_back_24)
                                    } else {
                                        painterResource(id = R.drawable.ic_baseline_arrow_back_dm_24)
                                    },
                                    contentDescription = null
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_baseline_menu_24),
                                    contentDescription = null,
                                )
                            }
                        }
                    },
                    title = {
                        if (currentDestination?.route == ProgramScreens.OneRepMax.route
                            || currentDestination?.route == ProgramScreens.RepeatCycle.route
                        ) {
                            Text(text = stringResource(id = R.string.programs_setup))
                        } else {
                            currentDestination?.route?.let { Text(text = it) }
                        }
                    }
                )
            },
            floatingActionButton = {
                when (currentDestination?.route) {
                    DrawerItems.Home.route -> {
                        ExtendedFloatingActionButton(
                            icon = { Icon(Icons.Filled.Add, null) },
                            text = {
                                Text(
                                    text = stringResource(id = R.string.programs_create_btn),
                                    color = MaterialTheme.colors.onSecondary
                                )
                            },
                            onClick = {
//                                navController.navigate(ProgramScreens.Create.route)
                            },
                            elevation = FloatingActionButtonDefaults.elevation(8.dp)
                        )
                    }
                    DrawerItems.WorkoutPrograms.route -> {
                        ExtendedFloatingActionButton(
                            icon = { Icon(Icons.Filled.Add, null) },
                            text = {
                                Text(
                                    text = stringResource(id = R.string.programs_create_btn),
                                    color = MaterialTheme.colors.onSecondary
                                )
                            },
                            onClick = {
                                navController.navigate(ProgramScreens.Create.route)
                            },
                            elevation = FloatingActionButtonDefaults.elevation(8.dp)
                        )
                    }
                    ProgramScreens.EditDay.route -> {
                        MultiFloatingActionButton(
                            fabIcon = ImageBitmap.imageResource(R.drawable.ic_add_white),
                            items = listOf(
                                MultiFabItem(
                                    identifier = "addExercise",
                                    icon = ImageBitmap.imageResource(R.drawable.ic_add_white),
                                    label = stringResource(id = R.string.program_add_exercise_item)
                                ),
                                MultiFabItem(
                                    identifier = "addRest",
                                    icon = ImageBitmap.imageResource(R.drawable.ic_add_white),
                                    label = stringResource(id = R.string.program_add_rest_item)
                                )
                            ), toState, true, { state ->
                                toState = state
                            }
                        ) { item ->
                            when (item.identifier) {
                                "addExercise" -> {
                                    navController.navigate(ProgramScreens.ExerciseSelection.route)
                                }
                                "addRest" -> {

                                }
                            }
                        }
                    }
                }
            },
            scaffoldState = scaffoldState,
            drawerContent = {
                Box(
                    modifier = Modifier
                        .height(180.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.secondaryVariant)
                ) {
                    // Top level composables for header

                }
                navDrawerItems.forEach { drawerItem ->
                    if (drawerItem.isHeader) {
                        DrawerRow(
                            isListItem = false,
                            rowContent = {
                                Text(
                                    text = stringResource(id = R.string.drawer_general),
                                    modifier = Modifier.padding(
                                        start = 16.dp,
                                        top = 4.dp,
                                        bottom = 4.dp
                                    ),
                                    style = headerTextStyle(),
                                    color = MaterialTheme.colors.onBackground
                                )
                            }
                        )
                    } else if (drawerItem.isSeperator) {
                        DrawerRow(
                            isListItem = false,
                            rowContent = {
                                Divider(
                                    color = MaterialTheme.colors.onBackground,
                                    thickness = 1.dp,
                                )
                            }
                        )
                    } else {
                        DrawerRow(
                            icon = drawerItem.icon,
                            titleRes = drawerItem.titleRes!!,
                            selected = currentDestination?.hierarchy?.any { it.route == drawerItem.route } == true,
                            onClick = {
                                scope.launch {
                                    scaffoldState.drawerState.close()
                                }
                                navController.navigate(drawerItem.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }

                }
            },
            bottomBar = {
                if (shouldShowBottomBarByCurrentRoute(currentDestination?.route)) {
                    BottomNavigation(
                        backgroundColor = MaterialTheme.colors.primary
                    ) {
                        bottomNavigationItems.forEach { screen ->
                            BottomNavigationItem(
                                selectedContentColor = MaterialTheme.colors.secondary,
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
            }
        ) { paddingValues ->
            NavHost(navController, startDestination = DrawerItems.Home.route) {
                composable(DrawerItems.Home.route) {
                    WorkoutScreen {
                        navController.navigate(ProgramScreens.EditDay.route)
                    }
                }
                composable(MainScreens.ProgressScreen.route) {
                    Text(text = "Progress Screen")
                }
                composable(MainScreens.HistoryScreen.route) {
                    Text(text = "History Screen")
                }

                // Navigation Drawer
                composable(DrawerItems.RateThisApp.route) {
                    Text(text = "RateThisApp Screen")
                }
                composable(DrawerItems.Purchases.route) {
                    Text(text = "Purchases Screen")
                }
                composable(DrawerItems.FAQ.route) {
                    Text(text = "FAQ Screen")
                }

                // Google Sync
                composable(DrawerItems.GoogleSync.route) {
                    GoogleSyncSignInScreen {
                        navController.navigate(GoogleSyncScreens.Settings.route)
                    }
                }
                composable(GoogleSyncScreens.Settings.route) {
                    GoogleSyncSettingsScreen()
                }

                // Settings
                composable(DrawerItems.Settings.route) {
                    GeneralSettingsScreen(navController = navController)
                }
                composable(SettingsScreens.About.route) {
                    AboutSettingsScreen()
                }
                composable(SettingsScreens.PrivacyPolicy.route) {
                    Text(text = "Privacy Policy")
                }
                composable(SettingsScreens.AppTheme.route) {
                    AppThemeSettingsScreen()
                }
                composable(SettingsScreens.Notifications.route) {
                    NotificationsSettingsScreen()
                }
                composable(SettingsScreens.DataManagement.route) {
                    DataMgmtSettingsScreen()
                }
                composable(SettingsScreens.ReportBug.route) {
                    BugReportSettingsScreen()
                }

                // Programs
                composable(DrawerItems.WorkoutPrograms.route) {
                    InitialProgramSetupScreen(navController = navController)
                }
                composable(ProgramScreens.OneRepMax.route) {
                    OneRepMaxProgramSetupScreen(navController = navController)
                }
                composable(ProgramScreens.RepeatCycle.route) {
                    RepeatProgramSetupScreen(navController = navController)
                }
                composable(ProgramScreens.Overview.route) {
                    ProgramOverviewScreen(navController = navController)
                }
                composable(ProgramScreens.Create.route) {
                    CreateProgramScreen(navController = navController)
                }
                composable(ProgramScreens.EditDay.route) {
                    EditDayProgramScreen()
                }
                composable(ProgramScreens.ExerciseSelection.route) {
                    ExerciseSelectionScreen(navController = navController)
                }
            }
        }
    }
}

