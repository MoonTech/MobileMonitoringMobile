package com.example.moontech.ui.viewmodel

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.camera.core.Preview.SurfaceProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.moontech.services.CameraService
import com.example.moontech.services.CameraServiceImpl
import com.example.moontech.ui.viewmodel.dataclasses.Room
import com.example.moontech.ui.viewmodel.dataclasses.RoomPrivilege
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val cameraService: MutableStateFlow<CameraService?> = MutableStateFlow(null)
    private val cameraServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            cameraService.value = (service as CameraServiceImpl.LocalBinder).getService()
            Log.i(TAG, "onServiceConnected: service produced")
        }
        override fun onServiceDisconnected(name: ComponentName?) { }
    }

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val isStreamingState: StateFlow<Boolean> =
        cameraService.filterNotNull().flatMapLatest { it.isStreaming }
            .stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = false)

    companion object {
        private const val TAG = "AppViewModel"
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as Application
                AppViewModel(application)
            }
        }
    }

    init {
        val context: Context = this.getApplication()
        val intent = Intent(context, CameraServiceImpl::class.java)
        context.startForegroundService(intent)
        context.bindService(intent, cameraServiceConnection, Context.BIND_AUTO_CREATE)
        Log.i(TAG, "service bind")
    }

    fun loginToRoomForWatching(roomCode: String, password: String? = null) {
        if (password != null) {
            val room = Room(roomCode, RoomPrivilege.Watch.code)
            addOrUpdateARoom(roomCode, room)
            updateWatchedRoom(room)
        } else {
            addOrUpdateARoom(roomCode, Room(roomCode))
        }
    }

    fun loginToRoomForTransmitting(roomCode: String, password: String? = null) {
        if (password != null) {
            val room = Room(roomCode, RoomPrivilege.Transmit.code)
            addOrUpdateARoom(roomCode, room)
            updateTransmittingRoom(room)
        } else {
            addOrUpdateARoom(roomCode, Room(roomCode))
        }
    }

    private fun addOrUpdateARoom(roomCode: String, room: Room) {
        _uiState.update { prevState ->
            prevState.copy(rooms = prevState.rooms.toMutableMap().apply {
                val roomToPut = get(roomCode)?.merge(room) ?: room
                put(roomCode, roomToPut)
            })
        }
    }

    private fun updateWatchedRoom(room: Room) {
        _uiState.update { prevState ->
            prevState.copy(watchedRoomCode = room.code)
        }
    }

    private fun updateTransmittingRoom(room: Room) {
        _uiState.update { prevState ->
            prevState.copy(transmittingRoomCode = room.code)
        }
    }

    override fun onCleared() {
        Log.i(TAG, "onCleared: clearing")
        cameraService.value?.stopPreview()
        this.getApplication<Application>().unbindService(cameraServiceConnection)
        super.onCleared()
    }

    fun startPreview(surfaceProvider: SurfaceProvider) {
        withCameraService {
            it.startPreview(surfaceProvider)
        }
    }

    fun stopPreview() {
        withCameraService {
            it.stopPreview()
        }
    }

    fun startStream(url: String) {
        withCameraService {
            it.startStream(url)
        }
    }

    fun stopStream() {
        withCameraService {
            it.stopStream()
        }
    }

    private fun withCameraService(block: (CameraService) -> Unit) {
        viewModelScope.launch {
            cameraService.filterNotNull().collect {
                block(it)
                this.cancel()
            }
        }
    }
}