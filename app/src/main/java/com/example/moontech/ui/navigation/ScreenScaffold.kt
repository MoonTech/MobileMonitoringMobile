package com.example.moontech.ui.navigation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val snackbarHostState = remember { SnackbarHostState() }
    val errorState = viewModel.errorState.collectAsState()
    LaunchedEffect(errorState.value) {
        Log.i(TAG, "ScreenScaffold: error launched ${errorState.value}")
        errorState.value.ifError {
            snackbarHostState.showSnackbar(it.errorMessage)
        }
    }
    Scaffold(modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            val visibleRoutes = setOf(
                Screen.MyRooms.Main.route,
                Screen.ExternalRooms.Main.route
            )
            if (currentRoute != null && currentRoute in visibleRoutes) {
                val text = Screen.valueOf(currentRoute).label
                Log.i(TAG, "ScreenScaffold: route extracted $text")
                Column(modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.primaryContainer))
                {
                    Text(
                        text = stringResource(text),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(8.dp)
                    )
                    Divider(modifier = Modifier.fillMaxWidth())
                }
            }
        },
        bottomBar = {
            val isStreaming by viewModel.isStreamingState.collectAsState()
            BottomNavigationBar(
                navigationItems = if (isStreaming) streamingNavigationItems else defaultNavigationItems,
                navigateTo = { screen ->
                    navController.navigate(screen.route) {
                        Log.i(
                            TAG,
                            "ScreenScaffold: ${navController.graph.findStartDestination().route}"
                        )
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
val externalRoomsNavigationItem =
    NavigationItem(screen = Screen.ExternalRooms, icon = Icons.Filled.LiveTv, showBadge = false)

val defaultNavigationItems: List<NavigationItem> =
    listOf(myRoomsNavigationItem, externalRoomsNavigationItem)


val streamingNavigationItems: List<NavigationItem> = listOf(
    myRoomsNavigationItem,
    externalRoomsNavigationItem
)