package com.example.moontech.ui.screens.watch

import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.moontech.ui.components.CenterScreen
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
            CenterScreen(modifier = modifier) {
                Text(text = "My Rooms")
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