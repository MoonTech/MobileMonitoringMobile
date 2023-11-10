package com.example.moontech.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val screen: Screen,
    val icon: ImageVector,
    val showBadge: Boolean,
    val acceptedScreens: List<Screen> = listOf()
) {

}