package com.example.moontech.ui.screens.watch

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.moontech.ui.components.LogOutDialog
import com.example.moontech.ui.navigation.Screen
import com.example.moontech.ui.navigation.navigateToScreenWithCode
import com.example.moontech.ui.screens.base.MainScreenBase
import com.example.moontech.ui.screens.myrooms.MyRoomsAddRoomScreen
import com.example.moontech.ui.screens.myrooms.SplashScreen
import com.example.moontech.ui.viewmodel.AppViewModel

private const val TAG = "MyRoomsGraph"
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
            val rooms by viewModel.myRooms.collectAsState()
            LaunchedEffect(loggedIn) {
                if (loggedIn == true) {
                    viewModel.fetchMyRooms()
                }
            }
            var showLogOutDialog by remember { mutableStateOf(false) }
            MainScreenBase(rooms = rooms,
                modifier = modifier,
                addRoom = {
                    navController.navigate(Screen.MyRooms.AddRoom.route)
                },
                onSettings = {
                },
                onClick = {
                    navController.navigateToScreenWithCode(Screen.Watch, it.code)
                },
                topBar = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
//                            .background(color = MaterialTheme.colorScheme.primaryContainer)
                            .minimumInteractiveComponentSize()
                            .padding(start = 8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(Screen.MyRooms.label),
                            style = MaterialTheme.typography.titleSmall,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = { showLogOutDialog = true }) {
                            Icon(imageVector = Icons.Filled.Logout, contentDescription = "Log out")
                        }
                        if (showLogOutDialog) {
                            LogOutDialog(dismissDialog = { showLogOutDialog = false }, logOut = {
                                viewModel.logOutUser()
                            })
                        }
                    }
                })
        }
        composable(Screen.MyRooms.AddRoom.route) {
            MyRoomsAddRoomScreen(onAddRoom = { code, password ->
                viewModel.addRoom(code, password)
                navController.popBackStack(Screen.MyRooms.Main.route, inclusive = false)
            }, emitError = { viewModel.emitError(it) }, modifier = modifier)
        }
        composable(Screen.MyRooms.Splash.route) {
            SplashScreen(
                viewModel = viewModel,
                navigateToLoginScreen = {
                    Log.i(TAG, "myRoomsGraph:navigate to login screen ")
                    navController.navigate(Screen.UserAuthorization.route) {
                        popUpTo(Screen.MyRooms.Splash.route) {
                            inclusive = true
                        }
                    }
                },
                navigateToMainScreen = {
                    Log.i(TAG, "myRoomsGraph: navigate to main screen")
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