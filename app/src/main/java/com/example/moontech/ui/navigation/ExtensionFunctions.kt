package com.example.moontech.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

fun NavController.navigateToScreenWithCode(
    screen: Screen,
    code: String,
    builder: NavOptionsBuilder.() -> Unit = { }
) {
    navigate(screen.route.replace("{code}", code), builder)
}