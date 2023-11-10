package com.example.moontech.ui.screens.userauth

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.moontech.ui.navigation.Screen
import com.example.moontech.ui.viewmodel.AppViewModel

fun NavGraphBuilder.userAuthorizationGraph(
    navController: NavController,
    viewModel: AppViewModel,
    modifier: Modifier
) {
    val startDestination = Screen.UserAuthorization.Login.route
    navigation(startDestination = startDestination, route = Screen.UserAuthorization.route) {
        composable(Screen.UserAuthorization.Login.route) {
            LoginScreen(
                modifier = modifier,
                navigateToSignUpScreen = {
                    navController.navigate(Screen.UserAuthorization.SignUp.route)
                },
                logIn = {

                }
            )
        }

        composable(Screen.UserAuthorization.SignUp.route) {
            SignUpScreen(modifier = modifier,
                signUp = {

                },
                navigateToLoginScreen = {
                    navController.navigate(Screen.UserAuthorization.Login.route) {
                        popUpTo(Screen.UserAuthorization.Login.route)
                        launchSingleTop  = true
                    }
                }
            )
        }
    }
}