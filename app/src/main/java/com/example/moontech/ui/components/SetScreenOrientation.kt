package com.example.moontech.ui.components

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

private const val TAG = "SetScreenOrientation"

@SuppressLint("WrongConstant")
@Composable
fun RememberScreenOrientation(
    orientation: Int = -1,
    onPortrait: () -> Unit,
    onLandscape: () -> Unit,
    onDispose: () -> Unit
) {
    val context = LocalContext.current
    var originalOrientation by rememberSaveable { mutableStateOf<Int?>(null) }
    DisposableEffect(true) {
        val activity = context.findActivity()
        if (originalOrientation != null) {
            originalOrientation = activity?.requestedOrientation
        }

        onDispose {
            Log.i(TAG, "RememberScreenOrientation: disposing")
            if (activity?.requestedOrientation != originalOrientation) {
                activity?.requestedOrientation = originalOrientation ?: SCREEN_ORIENTATION_PORTRAIT
                onDispose()
            }
        }
    }
    LaunchedEffect(orientation) {
        val activity = context.findActivity()
        when (orientation) {
            -1 -> {
                activity?.requestedOrientation = originalOrientation?: SCREEN_ORIENTATION_PORTRAIT
                onPortrait()
            }
            else -> {
                activity?.requestedOrientation = orientation
                onLandscape()
            }
        }
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun Context.hideSystemUi() {
    val window = findActivity()?.window ?: return
    val insetsController = WindowCompat.getInsetsController(window, window.decorView)

    insetsController.apply {
        hide(WindowInsetsCompat.Type.statusBars())
        hide(WindowInsetsCompat.Type.navigationBars())
        systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

fun Context.showSystemUi() {
    val window = findActivity()?.window ?: return
    val insetsController = WindowCompat.getInsetsController(window, window.decorView)

    insetsController.apply {
        show(WindowInsetsCompat.Type.statusBars())
        show(WindowInsetsCompat.Type.navigationBars())
        systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
    }
}