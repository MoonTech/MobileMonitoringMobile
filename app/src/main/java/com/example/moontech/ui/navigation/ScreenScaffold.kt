package com.example.moontech.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.VideoCameraBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moontech.ui.viewmodel.AppViewModel
import com.example.moontech.ui.viewmodel.AppViewModelFactoryProvider

private const val TAG = "ScreenScaffold"
@Composable
fun ScreenScaffold(modifier: Modifier = Modifier) {
    val viewModel: AppViewModel = viewModel(factory = AppViewModelFactoryProvider.Factory)
    val navController = rememberNavController()

    Scaffold(modifier = modifier,
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val isStreaming by viewModel.isStreamingState.collectAsState()

            BottomNavigationBar(
                navigationItems = if (isStreaming) streamingNavigationItems else defaultNavigationItems,
                navigateTo = { screen ->
                    navController.navigate(screen.route) {
                        Log.i(TAG, "ScreenScaffold: ${navController.graph.findStartDestination().route}")
                        popUpTo(navController.graph.id) {
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
            viewModel = viewModel,
            navController = navController,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        )
    }
}

val myRoomsNavigationItem =
    NavigationItem(
        screen = Screen.MyRooms,
        icon = Icons.Filled.Home,
        showBadge = false,
        acceptedScreens = listOf(Screen.UserAuthorization)
    )
val watchNavigationItem =
    NavigationItem(screen = Screen.Watch, icon = Icons.Filled.LiveTv, showBadge = false)
val transmitNavigationItem =
    NavigationItem(screen = Screen.Transmit, icon = Icons.Filled.VideoCameraBack, showBadge = false)

val defaultNavigationItems: List<NavigationItem> =
    listOf(myRoomsNavigationItem, watchNavigationItem, transmitNavigationItem)


val streamingNavigationItems: List<NavigationItem> = listOf(
    myRoomsNavigationItem,
    watchNavigationItem,
    transmitNavigationItem.copy(showBadge = true)
)