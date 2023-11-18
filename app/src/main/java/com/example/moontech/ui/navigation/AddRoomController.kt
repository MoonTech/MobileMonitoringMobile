package com.example.moontech.ui.navigation

import androidx.navigation.NavController

fun addRoom(currentRoute: String, navController: NavController) {
    when(currentRoute) {
        Screen.MyRooms.Main.route -> {
            navController.navigate(Screen.MyRooms.AddRoom.route)
        }
        Screen.Watch.Main.route -> {
            navController.navigate(Screen.Watch.AddRoom.route)
        }
        Screen.Transmit.Main.route -> {

        }
    }
}