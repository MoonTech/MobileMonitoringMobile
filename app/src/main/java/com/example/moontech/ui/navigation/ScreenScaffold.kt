package com.example.moontech.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.VideoCameraBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moontech.ui.viewmodel.AppViewModel

@Composable
fun ScreenScaffold(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Scaffold(modifier = modifier,
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()

            BottomNavigationBar(
                navigationItems = navigationItems,
                navigateTo = { screen ->
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                backStackEntry = navBackStackEntry
            )
        }) { padding ->
        AppNavigation(
            viewModel = viewModel(factory = AppViewModel.Factory),
            navController = navController,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        )
    }
}

val navigationItems: List<NavigationItem> =
    listOf(
        NavigationItem(screen = Screen.MyRooms, icon = Icons.Filled.Home),
        NavigationItem(screen = Screen.Watch, icon = Icons.Filled.LiveTv),
        NavigationItem(screen = Screen.Transmit, icon = Icons.Filled.VideoCameraBack)
    )