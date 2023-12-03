package com.example.moontech.ui.screens.userauth

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.moontech.ui.navigation.Screen
import com.example.moontech.ui.screens.watch.NavigateToSplashScreenOnLoggedInStateChanged
import com.example.moontech.ui.viewmodel.AppViewModel

fun NavGraphBuilder.userAuthorizationGraph(
    navController: NavController,
    viewModel: AppViewModel,
    modifier: Modifier
) {
    val startDestination = Screen.UserAuthorization.Login.route
    navigation(startDestination = startDestination, route = Screen.UserAuthorization.route) {
        composable(Screen.UserAuthorization.Login.route) {
            val loggedIn by viewModel.loggedInState.collectAsState()
            val scope = rememberCoroutineScope()
            NavigateToSplashScreenOnLoggedInStateChanged(loggedIn, navController)
            LoginScreen(
                modifier = modifier,
                navigateToSignUpScreen = {
                    navController.navigate(Screen.UserAuthorization.SignUp.route)
                },
                emitError = {
                    viewModel.emitError(it)
                },
                logIn = { username, password ->
                    viewModel.logInUser(username, password)
                }
            )
        }

        composable(Screen.UserAuthorization.SignUp.route) {
            val loggedIn by viewModel.loggedInState.collectAsState()
            NavigateToSplashScreenOnLoggedInStateChanged(loggedIn, navController)
            SignUpScreen(modifier = modifier,
                signUp = { username, password ->
                    viewModel.registerUser(username, password)
                },
                emitError = { viewModel.emitError(it) },
                navigateToLoginScreen = {
                    navController.navigate(Screen.UserAuthorization.Login.route) {
                        popUpTo(Screen.UserAuthorization.Login.route)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}