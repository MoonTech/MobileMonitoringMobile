package com.example.moontech.ui.navigation

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.VideoCameraBack
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moontech.ui.components.hideSystemUi
import com.example.moontech.ui.components.showSystemUi
import com.example.moontech.ui.viewmodel.AppViewModel
import com.example.moontech.ui.viewmodel.AppViewModelFactoryProvider
import kotlinx.coroutines.launch

private const val TAG = "ScreenScaffold"

@Composable
fun ScreenScaffold(modifier: Modifier = Modifier) {
    val viewModel: AppViewModel = viewModel(factory = AppViewModelFactoryProvider.Factory)
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val navBackStackEntries by navController.currentBackStack.collectAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val snackbarHostState = remember { SnackbarHostState() }
    val errorState by viewModel.errorState.collectAsState()
    var lastError by rememberSaveable { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    Log.i(TAG, "ScreenScaffold: $lastError, ${errorState.hashCode()}")
    if (errorState.hashCode() != lastError) {
        lastError = errorState.hashCode()
        LaunchedEffect(errorState) {
            scope.launch {
                Log.i(TAG, "ScreenScaffold: error launched $errorState")
                errorState.ifError {
                    snackbarHostState.showSnackbar(it.errorMessage)
                }
            }
        }
    }
    Scaffold(modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        bottomBar = {
            val isStreaming by viewModel.isStreamingState.collectAsState()
            val navigationVisible by viewModel.navigationVisible.collectAsState()
            val configuration = LocalConfiguration.current
            if (navigationVisible && configuration.orientation == ORIENTATION_PORTRAIT) {
                BottomNavigationBar(
                    navigationItems = if (isStreaming) streamingNavigationItems else defaultNavigationItems,
                    navigateTo = { screen ->
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    backStackEntries = navBackStackEntries
                )
            }
        }) { padding ->
        Row(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            val configuration = LocalConfiguration.current
            val isStreaming by viewModel.isStreamingState.collectAsState()
            val items = if (isStreaming) streamingNavigationItems else defaultNavigationItems
            if (configuration.orientation == ORIENTATION_LANDSCAPE) {
                val context = LocalContext.current
                DisposableEffect(true) {
                    context.hideSystemUi()
                    onDispose {
                        context.showSystemUi()
                    }
                }
                LandscapeNavigation(
                    backStackEntries = navBackStackEntries,
                    navigationItems = items,
                    navigateTo = { screen ->
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    })
            }
            AppNavigation(
                viewModel = viewModel,
                navController = navController,
                modifier = Modifier.fillMaxSize()
            )
        }
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
val transmitNavigationItem =
    NavigationItem(screen = Screen.Transmit, icon = Icons.Filled.VideoCameraBack, showBadge = false)

val defaultNavigationItems: List<NavigationItem> =
    listOf(myRoomsNavigationItem, externalRoomsNavigationItem, transmitNavigationItem)


val streamingNavigationItems: List<NavigationItem> = listOf(
    myRoomsNavigationItem,
    externalRoomsNavigationItem,
    transmitNavigationItem.copy(showBadge = true)
)