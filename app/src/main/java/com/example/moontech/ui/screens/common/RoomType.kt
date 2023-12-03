package com.example.moontech.ui.screens.common

import androidx.annotation.StringRes
import com.example.moontech.R

enum class RoomType(val index: Int, @StringRes val label: Int) {
    EXTERNAL(0, R.string.external), MY_ROOMS(1, R.string.my_rooms)
}