package com.example.moontech.ui.screens.watch

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.moontech.ui.navigation.Screen
import com.example.moontech.ui.screens.base.MainScreenBase
import com.example.moontech.ui.screens.myrooms.MyRoomsAddRoomScreen
import com.example.moontech.ui.screens.myrooms.SplashScreen
import com.example.moontech.ui.viewmodel.AppViewModel

private const val TAG = "MyRoomsGraph"
fun NavGraphBuilder.myRoomsGraph(
    navController: NavController,
    viewModel: AppViewModel,
    modifier: Modifier
) {
    val startDestination = Screen.MyRooms.Splash.route
    navigation(startDestination = startDestination, route = Screen.MyRooms.route) {
        composable(Screen.MyRooms.Main.route) {
            val loggedIn by viewModel.loggedInState.collectAsState()
            NavigateToSplashScreenOnLoggedInStateChanged(loggedIn, navController)
            val rooms by viewModel.myRooms.collectAsState()
            LaunchedEffect(loggedIn) {
                viewModel.fetchMyRooms()
            }
            MainScreenBase(rooms = rooms,
                modifier = modifier,
                addRoom = {
                    navController.navigate(Screen.MyRooms.AddRoom.route)
                },
                onSettings = {},
                onTransmit = { },
                onWatch = {})
        }
        composable(Screen.MyRooms.AddRoom.route) {
            MyRoomsAddRoomScreen(onAddRoom = { code, password ->
                viewModel.addRoom(code, password)
                navController.popBackStack(Screen.MyRooms.Main.route, inclusive = false)
            }, modifier = modifier)
        }
        composable(Screen.MyRooms.Splash.route) {
            SplashScreen(
                viewModel = viewModel,
                navigateToLoginScreen = {
                    Log.i(TAG, "myRoomsGraph:navigate to login screen ")
                    navController.navigate(Screen.UserAuthorization.route) {
                        popUpTo(Screen.MyRooms.Splash.route) {
                            inclusive = true
                        }
                    }
                },
                navigateToMainScreen = {
                    Log.i(TAG, "myRoomsGraph: navigate to main screen")
                    navController.navigate(Screen.MyRooms.Main.route) {
                        popUpTo(Screen.MyRooms.Splash.route) {
                            inclusive = true
                        }
                    }
                })
        }
    }
}

@Composable
fun NavigateToSplashScreenOnLoggedInStateChanged(loggedIn: Boolean?, navController: NavController) {
    val firstLoggedIn = rememberSaveable() {
        mutableStateOf(loggedIn)
    }
    LaunchedEffect(loggedIn) {
        if (loggedIn != firstLoggedIn.value) {
            navController.navigate(Screen.MyRooms.Splash.route) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
        }
    }
}