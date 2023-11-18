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
import androidx.lifecycle.viewModelScope
import com.example.moontech.data.dataclasses.Result
import com.example.moontech.data.dataclasses.Room
import com.example.moontech.data.dataclasses.RoomData
import com.example.moontech.data.dataclasses.User
import com.example.moontech.data.dataclasses.UserData
import com.example.moontech.data.repository.RoomRepository
import com.example.moontech.data.repository.UserRepository
import com.example.moontech.data.store.RoomDataStore
import com.example.moontech.data.store.UserDataStore
import com.example.moontech.services.CameraService
import com.example.moontech.services.CameraServiceImpl
import com.example.moontech.ui.viewmodel.dataclasses.WatchedRoomsController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(
    application: Application,
    private val userRepository: UserRepository,
    private val userDataStore: UserDataStore,
    private val roomRepository: RoomRepository,
    private val roomDataStore: RoomDataStore
) : AndroidViewModel(application), MyRoomsController, WatchedRoomsController {
    companion object {
        private const val TAG = "AppViewModel"
    }

    private val cameraService: MutableStateFlow<CameraService?> = MutableStateFlow(null)
    private val cameraServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            cameraService.value = (service as CameraServiceImpl.LocalBinder).getService()
            Log.i(TAG, "onServiceConnected: service produced")
        }

        override fun onServiceDisconnected(name: ComponentName?) {}
    }

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    private val _myRooms: MutableStateFlow<List<Room>> = MutableStateFlow(listOf())
    val myRooms: StateFlow<List<Room>> = _myRooms

    @OptIn(ExperimentalCoroutinesApi::class)
    val isStreamingState: StateFlow<Boolean> =
        cameraService.filterNotNull().flatMapLatest { it.isStreaming }
            .stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = false)
    val loggedInState: StateFlow<Boolean?> =
        userDataStore.userData.map {
            it?.let { true } ?: false
        }.stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = null)
    override val watchedRooms: StateFlow<List<RoomData>> =
        roomDataStore.rooms
            .stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = listOf())

    init {
        val context: Context = this.getApplication()
        val intent = Intent(context, CameraServiceImpl::class.java)
        context.startForegroundService(intent)
        context.bindService(intent, cameraServiceConnection, Context.BIND_AUTO_CREATE)
        fetchMyRooms()
        Log.i(TAG, "service bind")
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

    fun logInUser(login: String, password: String) {
        val user = User(login, password)
        viewModelScope.launch {
            Log.i(TAG, "logInUser: ")
            if (userRepository.login(user)) {
                Log.i(TAG, "logInUser: user logged in")
                userDataStore.saveUserData(UserData("test_token"))
            }
        }
    }

    fun registerUser(login: String, password: String) {
        val user = User(login, password)
        viewModelScope.launch {
            Log.i(TAG, "registerUser:")
            if (userRepository.register(user)) {
                Log.i(TAG, "registerUser: user registered")
                logInUser(login, password)
            }
        }
    }

    private fun fetchMyRooms() {
        viewModelScope.launch {
            roomRepository
                .getRooms()
                .collect { result ->
                    if (result is Result.Success) {
                        _myRooms.emit(result.data)
                    }
                }
        }
    }

    fun logOutUser() {
        viewModelScope.launch {
            userDataStore.clearUserData()
        }
    }

    override fun addRoom(code: String, password: String?) {
        viewModelScope.launch {
            roomRepository.addRoom(code, password)
        }
    }

    override fun addWatchedRoom(code: String, password: String) {
        viewModelScope.launch {
            roomDataStore.addRoomData(RoomData(code))
        }
    }


}