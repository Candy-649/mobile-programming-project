package com.example.healthyhabittracker

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.healthyhabittracker.data.DataSource
import com.example.healthyhabittracker.ui.AppViewModelProvider
import com.example.healthyhabittracker.ui.MyViewModel
import com.example.healthyhabittracker.ui.SettingsViewModel
import com.example.healthyhabittracker.ui.WeatherViewModel
import com.example.healthyhabittracker.ui.components.DrawerState
import com.example.healthyhabittracker.ui.components.PushDrawer
import com.example.healthyhabittracker.ui.screen.HomeScreen
import com.example.healthyhabittracker.ui.screen.LoginScreen
import com.example.healthyhabittracker.ui.screen.ProfileContentDrawer
import com.example.healthyhabittracker.ui.screen.ProfileScreen
import com.example.healthyhabittracker.ui.screen.SetStepGoalScreen
import com.example.healthyhabittracker.ui.screen.SettingsScreen
import com.example.healthyhabittracker.ui.screen.SimpleInputDialog
import com.example.healthyhabittracker.ui.screen.WaterReminderEntryScreen
import com.example.healthyhabittracker.ui.screen.WeatherScreen
import com.example.healthyhabittracker.ui.theme.HealthyHabitTrackerTheme
import com.example.healthyhabittracker.ui.view.AuthViewModel
import com.example.healthyhabittracker.ui.view.Profile.EditProfileViewModel
import com.example.healthyhabittracker.ui.view.Profile.ProfileViewModel
import com.example.healthyhabittracker.ui.view.StepData.StepDataEntryViewModel
import com.example.healthyhabittracker.ui.view.WaterReminder.WaterReminderEditViewModel
import com.example.healthyhabittracker.ui.view.WaterReminder.WaterReminderEntryViewModel
import com.google.android.gms.location.LocationServices
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch


enum class HealthScreen(@StringRes val title: Int){
    Home(title = R.string.main_screen_name),
    Weather(title = R.string.water_scree_name),
    Login(title = R.string.log_in),
    Profile(title = R.string.profile_screen_name),
    SetWaterReminder(title = R.string.set_goal),
    SetStepGoal(title = R.string.set_step_goal),
    Settings(title = R.string.setting),
    EditNickname(title = R.string.edit_nickname),
    EditMobileNumber(title = R.string.edit_mobile_number)
}

@Composable
fun BottomAppBar(
    currentScreen: HealthScreen,
    navController: NavHostController
){
    NavigationBar {
        NavigationBarItem(
            icon = {Icon(
                imageVector = Icons.Default.Cloud,
                contentDescription = "Weather"
            )},
            label = {Text("Weather")},
            selected = currentScreen == HealthScreen.Weather,
            onClick = {
                navController.navigate(HealthScreen.Weather.name)
            }
        )
        NavigationBarItem(
            icon = { Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Home"
            )},
            label = { Text("Home")},
            selected = currentScreen == HealthScreen.Home,
            onClick = {
                navController.navigate(HealthScreen.Home.name)
            }
        )
    }
}





@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicLayout(
    viewModel: MyViewModel = viewModel(),
    settingsViewModel: SettingsViewModel = viewModel(),
    weatherViewModel: WeatherViewModel = viewModel(),
    waterReminderEntryViewModel: WaterReminderEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    waterReminderEditViewModel: WaterReminderEditViewModel = viewModel(factory = AppViewModelProvider.Factory),
    stepDataEntryViewModel: StepDataEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    editProfileViewModel: EditProfileViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
){
    val currentUser = Firebase.auth.currentUser
    val startRoute = if (currentUser != null) HealthScreen.Home.name else HealthScreen.Login.name
    val scope = rememberCoroutineScope()
    val drawerState = remember { DrawerState() }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = HealthScreen.valueOf(
        backStackEntry?.destination?.route?: HealthScreen.Home.name
    )
    LaunchedEffect(Unit) {
        stepDataEntryViewModel.syncHistoricalDataToFireBase()
    }
    MaterialTheme(
        colorScheme = if (settingsViewModel.isAppDarkTheme) darkColorScheme() else lightColorScheme()
    ) {
        PushDrawer(
            drawerState = drawerState,
            drawerContent = {
                ProfileContentDrawer(
                    name = editProfileViewModel.nickname,
                    avatar = editProfileViewModel.avatarUri.toUri(),
                    onEditClick = {
                        navController.navigate(HealthScreen.Profile.name)
                        drawerState.close()
                                  },
                    onSetWaterReminder = {
                        navController.navigate(HealthScreen.SetWaterReminder.name)
                        drawerState.close()
                                         },
                    onSetDailyStepClick = {
                        navController.navigate(HealthScreen.SetStepGoal.name)
                        drawerState.close()
                                          },
                    onSettingClick = {
                        navController.navigate(HealthScreen.Settings.name)
                        drawerState.close()
                    }
                )
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(stringResource(id = currentScreen.title)) },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        }
                    )
                },
                bottomBar = {
                    BottomAppBar(currentScreen = currentScreen, navController = navController)
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = startRoute,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        composable(route = HealthScreen.Home.name) {
                            val reminderList by waterReminderEditViewModel.reminderList.collectAsState()
                            val context = LocalContext.current
                            HomeScreen(
                                viewModel.timeText,
                                viewModel.dateText,
                                waterReminderList = reminderList,
                                stepDataEntryViewModel.stepProgress.collectAsState().value,
                                stepDataEntryViewModel.stepData.collectAsState().value.steps.toString(),
                                stepGoal = stepDataEntryViewModel.stepData.collectAsState().value.goal,
                                editingDetails = waterReminderEditViewModel.uiState.waterReminderDetails,
                                onReminderClicked = { navController.navigate(HealthScreen.SetWaterReminder.name) },
                                onEditClicked = {
                                    waterReminderEditViewModel.setUiState(it)
                                },
                                onValueChange = waterReminderEditViewModel::updateUiState,
                                onConfirmClicked = {
                                    scope.launch {
                                        waterReminderEditViewModel.updateReminder(context)
                                    }
                                },
                                onDelete = {
                                    scope.launch {
                                        waterReminderEditViewModel.deleteReminder(it, context)
                                    }
                                }
                            )
                        }
                        composable(route = HealthScreen.Weather.name) {
                            val context = LocalContext.current
                            val locationPermissionLauncher = rememberLauncherForActivityResult(
                                ActivityResultContracts.RequestMultiplePermissions()
                            ) { permissions ->
                                val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
                                val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                                weatherViewModel.fetchWeatherWithLocation(
                                    LocationServices.getFusedLocationProviderClient(context),
                                    fineGranted, coarseGranted, BuildConfig.MAPS_API_KEY
                                )
                            }
                            val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            val hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            LaunchedEffect(Unit) {
                                if (!hasFine && !hasCoarse) {
                                    locationPermissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                }else {
                                    weatherViewModel.fetchWeatherWithLocation(
                                        LocationServices.getFusedLocationProviderClient(context),
                                        hasFine, hasCoarse,
                                        apiKey = "AIzaSyC76PR2xOqWqW8nxpqDYKHWgjwejTYOixI"
                                    )
                                }
                            }
                            WeatherScreen(
                                temperature = weatherViewModel.temperature,
                                feelsLikeTemperature = weatherViewModel.feelsLikeTemperature,
                                description = weatherViewModel.description,
                                isDaytimeNow = weatherViewModel.isDaytimeNow,
                                airQualityIndex = weatherViewModel.airQualityIndex,
                                dominantPollutantText = weatherViewModel.dominantPollutantText,
                                advice = weatherViewModel.advice
                            )
                        }
                        composable(route = HealthScreen.Login.name) {
                            LoginScreen(viewModel = AuthViewModel()) {
                                navController.navigate(HealthScreen.Home.name)
                            }
                        }
                        composable(route = HealthScreen.Profile.name) {
                            val context = LocalContext.current
                            ProfileScreen(
                                avatar = editProfileViewModel.avatarUri.toUri(),
                                nickname = editProfileViewModel.nickname,
                                onAvatarSelected = {newUri ->
                                    editProfileViewModel.updateAvatar(newUri, context)
                                },
                                onNicknameClick = {
                                    navController.navigate(HealthScreen.EditNickname.name)
                                })
                        }
                        composable(route = HealthScreen.SetWaterReminder.name) {
                            val context = LocalContext.current
                            WaterReminderEntryScreen(
                                waterReminderUiState = waterReminderEntryViewModel.waterReminderUiState,
                                onReminderValueChange = waterReminderEntryViewModel::updateUiState,
                                onConfirmClicked = {
                                    scope.launch {
                                        waterReminderEntryViewModel.saveWaterReminder(context)
                                    }

                                    navController.navigate(HealthScreen.Home.name)
                                }
                            )
                        }
                        composable(route = HealthScreen.SetStepGoal.name) {
                            SetStepGoalScreen(
                                title = "Set Step Goal: ",
                                options = DataSource.stepCount,
                                unit = "steps",
                                tempGoal = stepDataEntryViewModel.tempGoal,
                                onCancelClicked = {
                                    navController.navigate(HealthScreen.Home.name)
                                }
                            ) { value ->
                                stepDataEntryViewModel.saveGoal(value)
                                navController.navigate(HealthScreen.Home.name)
                            }
                        }
                        composable(route = HealthScreen.Settings.name) {
                            val context = LocalContext.current
                            var showLauncher by remember { mutableStateOf(false) }
                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.RequestPermission()
                            ) { granted ->
                                if (granted) {
                                    Toast.makeText(context, "Permission granted", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                                }
                            }

                            val isDarkTheme = settingsViewModel.isAppDarkTheme

                            SettingsScreen(
                                notificationsOn = viewModel.notificationEnabled,
                                darkMode = isDarkTheme,
                                darkModeFollowSystem = settingsViewModel.followSystem,
                                onEditProfile = { navController.navigate(HealthScreen.Profile.name) },
                                onNotificationToggle = { viewModel.setNotificationsEnabled(!viewModel.notificationEnabled) },
                                onDarkModeToggle = { settingsViewModel.setDarkModeEnabled(!settingsViewModel.darkModeEnabled) },
                                onFollowSystemToggle = { settingsViewModel.setFollowSystem(!settingsViewModel.followSystem) },
                                onNotificationPermissionClick = {
                                    settingsViewModel.onNotificationPermissionClicked(context){
                                        showLauncher = true
                                    }
                                }
                            )
                            LaunchedEffect(showLauncher) {
                                if (showLauncher) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    }
                                    showLauncher = false
                                }
                            }
                        }
                        composable(route = HealthScreen.EditNickname.name){
                            SimpleInputDialog(
                                label = "Please enter new nickname",
                                initialText = viewModel.nickname,
                                onCancel = { navController.navigate(HealthScreen.Profile.name)}
                            ) { newName ->
                                editProfileViewModel.updateNickname(newName){
                                    navController.navigate(HealthScreen.Profile.name)
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}





@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BasicNightPreview() {
    HealthyHabitTrackerTheme {
        Surface{

        }

    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun BasicLightPreview() {
    HealthyHabitTrackerTheme {
        Surface{

        }
    }
}