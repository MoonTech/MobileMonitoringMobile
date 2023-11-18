package com.example.moontech.ui.navigation

import androidx.annotation.StringRes
import com.example.moontech.R

sealed class Screen(val route: String, @StringRes val label: Int) {
    object MyRooms : Screen("my_rooms", R.string.my_rooms) {
        object Splash : Screen("my_rooms_splash", R.string.my_rooms)
        object Main : Screen("my_rooms_main", R.string.my_rooms)
        object AddRoom : Screen("my_rooms_add_room", R.string.my_rooms)
    }

    object Watch : Screen("watch", R.string.watch) {
        object Main : Screen("watch_main", R.string.watch)
        object AddRoom : Screen("watch_add_room", R.string.watch)
        object Watching : Screen("watch_watching", R.string.watch)
    }

    object Transmit : Screen("transmit", R.string.transmit) {
        object Main : Screen("transmit_main", R.string.transmit)
        object AddRoom : Screen("transmit_add_room", R.string.transmit)
        object Camera : Screen("transmit_camera", R.string.transmit)
    }

    object UserAuthorization : Screen("user_authorization", R.string.login) {
        object Login : Screen("user_authorization_login", R.string.login)

        object SignUp : Screen("user_authorization_sign_up", R.string.login)
    }

    companion object {
        val screens = listOf(
            MyRooms.Splash,
            MyRooms.Main,
            MyRooms.AddRoom,
            MyRooms,
            Watch,
            Watch.Main,
            Watch.AddRoom,
            Watch.Watching,
            Transmit,
            Transmit.Main,
            Transmit.AddRoom,
            Transmit.Camera,
            UserAuthorization,
            UserAuthorization.Login,
            UserAuthorization.SignUp
        )
    }

    fun valueOf(route: String): Screen {
        return screens.first { screen -> screen.route == route }
    }
}