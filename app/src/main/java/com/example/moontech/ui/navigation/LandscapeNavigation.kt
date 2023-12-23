package com.example.moontech.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry

@Composable
fun LandscapeNavigation(
    modifier: Modifier = Modifier,
    backStackEntries: List<NavBackStackEntry>,
    navigationItems: List<NavigationItem>,
    navigateTo: (screen: Screen) -> Unit,
) {

    NavigationRail {
        navigationItems.forEachIndexed { i, item ->
            val selected = backStackEntries.map { it.destination.route }.any { route ->
                route == item.screen.route || item.acceptedScreens.any { it.route == route }
            }
            NavigationRailItem(
                selected = selected,
                onClick = { navigateTo(item.screen) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.screen.route
                    )
                })
        }
    }
}