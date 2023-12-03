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

fun NavController.navigateWithParams(
    screen: Screen,
    params: Map<String, Any?>,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    val parametrizedRoute = params.entries.fold(screen.route) { s, entry ->
        s.replace("{${entry.key}}", entry.value.toString())

    }
    navigate(parametrizedRoute, builder)
}