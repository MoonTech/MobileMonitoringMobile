package com.example.moontech.ui.viewmodel.dataclasses

enum class RoomPrivilege(val code: Int) {
    Watch(1), Transmit(2), Manage(4);

    companion object {
        fun checkPrivilege(privileges: Int, privilegeToCheck: RoomPrivilege): Boolean {
            return privileges and privilegeToCheck.code >  0
        }

        fun mergePrivileges(privileges1: Int, privileges2: Int): Int {
            return privileges1 or privileges2
        }
    }
}