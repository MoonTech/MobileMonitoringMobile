package com.example.moontech.ui.viewmodel.dataclasses

import com.example.moontech.ui.viewmodel.dataclasses.RoomPrivilege.Manage
import com.example.moontech.ui.viewmodel.dataclasses.RoomPrivilege.Transmit

data class Room(
    val code: String,
    val privileges: Int = 0,
) {

    fun canWatch(): Boolean {
        return RoomPrivilege.checkPrivilege(privileges, RoomPrivilege.Watch)
    }

    fun canTransmit(): Boolean {
        return RoomPrivilege.checkPrivilege(privileges, Transmit)
    }

    fun canManage(): Boolean {
        return RoomPrivilege.checkPrivilege(privileges, Manage)
    }

    fun merge(room: Room): Room {
        return Room(
            code = this.code,
            privileges = RoomPrivilege.mergePrivileges(
                this.privileges,
                room.privileges
            )
        )
    }
}