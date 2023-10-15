package com.example.moontech.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moontech.ui.screens.home.HomeScreen
import com.example.moontech.ui.screens.roomlogin.RoomLoginScreen
import com.example.moontech.ui.viewmodel.AppViewModel

@Composable
fun AppNavigation(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(route = Screen.Main.route) {
            HomeScreen(
                onCreateRoom = { },
                onAddCamera = { },
                onWatchTransmission = { navController.navigate(Screen.RoomLogin.route) },
                modifier = modifier)
        }
        composable(route = Screen.RoomLogin.route) {
            RoomLoginScreen(modifier = modifier, viewModel = viewModel)
        }

    }

}