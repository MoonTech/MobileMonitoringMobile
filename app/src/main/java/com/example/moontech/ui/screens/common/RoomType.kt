package com.example.moontech.ui.screens.common

import androidx.annotation.StringRes
import com.example.moontech.R

enum class RoomType(val index: Int, @StringRes val label: Int) {
    MY_ROOM(0, R.string.my_rooms), EXTERNAL(1, R.string.external)
}