package com.example.moontech.ui.screens.watch

import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.moontech.ui.components.CenterScreen
import com.example.moontech.ui.navigation.Screen
import com.example.moontech.ui.viewmodel.AppViewModel

fun NavGraphBuilder.myRoomsGraph(
    navController: NavController,
    viewModel: AppViewModel,
    modifier: Modifier
) {
    val startDestination = Screen.MyRooms.Main.route
    navigation(startDestination = startDestination, route = Screen.MyRooms.route) {
        composable(Screen.MyRooms.Main.route) {
            CenterScreen(modifier = modifier) {
                Text(text = "My Rooms")
            }
        }
        composable(Screen.MyRooms.AddRoom.route) {

        }
    }
}