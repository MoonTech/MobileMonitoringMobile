@file:OptIn(ExperimentalPermissionsApi::class)

package com.example.moontech.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.moontech.ui.screens.external.externalRoomsGraph
import com.example.moontech.ui.screens.transmit.transmitGraph
import com.example.moontech.ui.screens.userauth.userAuthorizationGraph
import com.example.moontech.ui.screens.watch.myRoomsGraph
import com.example.moontech.ui.screens.watch.watchGraph
import com.example.moontech.ui.viewmodel.AppViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@Composable
fun AppNavigation(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = Screen.MyRooms.route) {
        transmitGraph(navController = navController ,viewModel = viewModel, modifier = modifier)
        watchGraph(navController = navController ,viewModel = viewModel, modifier = modifier)
        myRoomsGraph(navController = navController ,viewModel = viewModel, modifier = modifier)
        userAuthorizationGraph(navController = navController ,viewModel = viewModel, modifier = modifier)
        externalRoomsGraph(navController = navController ,viewModel = viewModel, modifier = modifier)
    }
}