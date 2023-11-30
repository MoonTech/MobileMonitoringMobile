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
import com.example.moontech.data.dataclasses.AppError
import com.example.moontech.data.dataclasses.CameraRequest
import com.example.moontech.data.dataclasses.ManagedRoom
import com.example.moontech.data.dataclasses.Room
import com.example.moontech.data.dataclasses.RoomCamera
import com.example.moontech.data.dataclasses.RoomCreationRequest
import com.example.moontech.data.dataclasses.RoomData
import com.example.moontech.data.dataclasses.User
import com.example.moontech.data.dataclasses.WatchedRoom
import com.example.moontech.data.store.RoomCameraDataStore
import com.example.moontech.data.store.RoomDataStore
import com.example.moontech.data.store.UserDataStore
import com.example.moontech.services.CameraService
import com.example.moontech.services.CameraServiceImpl
import com.example.moontech.services.web.CameraApiService
import com.example.moontech.services.web.RoomApiService
import com.example.moontech.services.web.UserApiService
import com.example.moontech.ui.screens.common.RoomType
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
    private val userDataStore: UserDataStore,
    private val roomDataStore: RoomDataStore,
    private val roomCameraDataStore: RoomCameraDataStore,
    private val userApiService: UserApiService,
    private val roomApiService: RoomApiService,
    private val cameraApiService: CameraApiService
) : AndroidViewModel(application), MyRoomsController, WatchedRoomsController, CameraController {
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
    private val _myRooms: MutableStateFlow<List<ManagedRoom>> =
        MutableStateFlow(listOf())
    val myRooms: StateFlow<List<ManagedRoom>> = _myRooms
    private val _errorState: MutableStateFlow<AppError> = MutableStateFlow(AppError.Empty())
    val errorState: StateFlow<AppError> = _errorState

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

    override val externalRoomCameras: StateFlow<List<RoomCamera>> = roomCameraDataStore.roomCameras
        .map { it.filter { camera -> camera.roomType == RoomType.EXTERNAL } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = listOf())

    override val myRoomCameras: StateFlow<List<RoomCamera>> = roomCameraDataStore.roomCameras
        .map { it.filter { camera -> camera.roomType == RoomType.MY_ROOMS } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = listOf())

    override val roomCameras: StateFlow<List<RoomCamera>> = roomCameraDataStore.roomCameras
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
            userApiService.logIn(user).onSuccessWithErrorHandling {
                userDataStore.save(it)
                Log.i(TAG, "logInUser: user logged in")
            }
        }
    }

    fun registerUser(login: String, password: String) {
        val user = User(login, password)
        viewModelScope.launch {
            Log.i(TAG, "registerUser:")
            userApiService.register(user).onSuccessWithErrorHandling {
                userDataStore.save(it)
                Log.i(TAG, "registerUser: user registered")
            }
        }
    }

    fun fetchMyRooms() {
        viewModelScope.launch {
            roomApiService.getUserRooms().onSuccessWithErrorHandling {
                _myRooms.emit(it.rooms)
            }
        }
    }

    fun logOutUser() {
        viewModelScope.launch {
            userDataStore.clear()
        }
    }

    override fun addRoom(code: String, password: String) {
        viewModelScope.launch {
            roomApiService.createRoom(RoomCreationRequest(code, password))
                .onSuccessWithErrorHandling {
                    fetchMyRooms()
                }
        }
    }

    override fun addWatchedRoom(code: String, password: String) {
        viewModelScope.launch {
            val watchRoomResponse: kotlin.Result<WatchedRoom> = roomApiService.watchRoom(code)
            watchRoomResponse.onSuccessWithErrorHandling {
                roomDataStore.add(RoomData(code))
            }
        }
    }

    override fun removeWatchedRoom(code: String) {
        viewModelScope.launch {
            roomDataStore.delete(RoomData(code))
        }
    }

    override fun removeRoomCamera(roomCamera: RoomCamera) {
        viewModelScope.launch {
            roomCameraDataStore.delete(roomCamera)
        }
    }

    override fun addRoomCamera(code: String, password: String) {
        viewModelScope.launch {
            cameraApiService.addCamera(CameraRequest(code, password))
                .onSuccessWithErrorHandling {
                    val roomCamera = RoomCamera(
                        code = code,
                        token = it.cameraToken,
                        url = it.cameraUrl,
                        roomType = RoomType.EXTERNAL
                    )
                    roomCameraDataStore.add(roomCamera)
                }
        }
    }

    suspend fun emitError(error: AppError) {
        _errorState.emit(error)
    }

    private suspend inline fun <T> Result<T>.onSuccessWithErrorHandling(block: (T) -> Unit = {}) {
        onSuccess {
            block(it)
        }
        onFailure {
            Log.i(TAG, "onSuccessWithErrorHandling: handling failure")
            _errorState.emit(AppError.Error("Something went wrong"))
        }
    }

}