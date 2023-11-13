package com.example.moontech.ui.screens.watch

import androidx.compose.material3.Text
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
import com.example.moontech.ui.components.CenterScreen
import com.example.moontech.ui.components.PrimaryButton
import com.example.moontech.ui.navigation.Screen
import com.example.moontech.ui.screens.myrooms.SplashScreen
import com.example.moontech.ui.viewmodel.AppViewModel

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
            CenterScreen(modifier = modifier) {
                Text(text = "My Rooms")
                PrimaryButton(text = "Log out", onClick = { viewModel.logOutUser() })
            }
        }
        composable(Screen.MyRooms.AddRoom.route) {

        }
        composable(Screen.MyRooms.Splash.route) {
            SplashScreen(
                viewModel = viewModel,
                navigateToLoginScreen = {
                    navController.navigate(Screen.UserAuthorization.route) {
                        popUpTo(Screen.MyRooms.Splash.route) {
                            inclusive = true
                        }
                    }
                },
                navigateToMainScreen = {
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