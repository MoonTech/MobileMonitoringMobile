package com.example.moontech.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy

@Composable
fun BottomNavigationBar(
    backStackEntry: NavBackStackEntry?,
    navigationItems: List<NavigationItem>,
    navigateTo: (screen: Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        navigationItems.forEach {
            NavigationBarItem(
                selected = backStackEntry?.destination?.hierarchy?.any {
                        destination -> destination.route == it.screen.route
                } ?: false,
                onClick = { navigateTo(it.screen) },
                label = { Text(text = stringResource(it.screen.label)) },
                icon = {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = stringResource(it.screen.label)
                    )
                })
        }
    }
}