package com.example.moontech.ui.navigation

import androidx.annotation.StringRes
import com.example.moontech.R

sealed class Screen(val route: String, @StringRes val label: Int) {
    object MyRooms : Screen("my_rooms", R.string.my_rooms) {
        object Splash : Screen("my_rooms_splash", R.string.my_rooms)
        object Main : Screen("my_rooms_main", R.string.my_rooms)
        object AddRoom : Screen("my_rooms_add_room", R.string.my_rooms)
    }
    object ExternalRooms : Screen("ext_rooms", R.string.external) {
        object Main : Screen("ext_rooms_main", R.string.external)
        object AddRoom : Screen("ext_rooms_add_room", R.string.external)
    }

    object Watch : Screen("watch/{code}", R.string.watch) {
        object Watching : Screen("watch_watching/{code}", R.string.watch)
    }

    object  Transmit : Screen("transmit", R.string.transmit) {
        object AddRoom : Screen("transmit_add_room", R.string.transmit)
        object AddCamera : Screen("transmit_add_camera/{code}/{password}", R.string.transmit)
        object SelectCamera : Screen("transmit_select_camera", R.string.transmit)
        object Camera : Screen("transmit_camera/{code}", R.string.transmit)
    }

    object UserAuthorization : Screen("user_authorization", R.string.login) {
        object Login : Screen("user_authorization_login", R.string.login)

        object SignUp : Screen("user_authorization_sign_up", R.string.login)
    }

    companion object {
        private const val TAG = "Screen"
        val screens = listOf(
            MyRooms.Splash,
            MyRooms.Main,
            MyRooms.AddRoom,
            Watch.Watching,
            Transmit.Camera,
            UserAuthorization,
            UserAuthorization.Login,
            UserAuthorization.SignUp,
            ExternalRooms.Main,
            ExternalRooms.AddRoom
        )
        fun valueOf(route: String): Screen {
            return screens.first { screen -> screen.route == route }
        }
    }
}