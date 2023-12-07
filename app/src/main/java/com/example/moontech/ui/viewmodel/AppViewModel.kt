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
import com.example.moontech.R
import com.example.moontech.data.dataclasses.AppState
import com.example.moontech.data.dataclasses.CameraRequest
import com.example.moontech.data.dataclasses.ManagedRoom
import com.example.moontech.data.dataclasses.RoomCamera
import com.example.moontech.data.dataclasses.RoomCreationRequest
import com.example.moontech.data.dataclasses.RoomData
import com.example.moontech.data.dataclasses.User
import com.example.moontech.data.dataclasses.WatchRequest
import com.example.moontech.data.dataclasses.WatchedRoom
import com.example.moontech.data.store.RoomCameraDataStore
import com.example.moontech.data.store.RoomDataStore
import com.example.moontech.data.store.UserDataStore
import com.example.moontech.services.camera.CameraService
import com.example.moontech.services.camera.CameraServiceImpl
import com.example.moontech.services.web.CameraApiService
import com.example.moontech.services.web.RoomApiService
import com.example.moontech.services.web.UserApiService
import com.example.moontech.ui.screens.common.RoomType
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(
    application: Application,
    private val userDataStore: UserDataStore,
    private val roomDataStore: RoomDataStore,
    private val roomCameraDataStore: RoomCameraDataStore,
    private val userApiService: UserApiService,
    private val roomApiService: RoomApiService,
    private val cameraApiService: CameraApiService
) : AndroidViewModel(application), MyRoomsController, ExternalRoomsController, CameraController,
    WatchController {
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
    val uiState = _uiState.asStateFlow()
    private val _myRooms: MutableStateFlow<List<ManagedRoom>> = MutableStateFlow(listOf())
    val myRooms = _myRooms.asStateFlow()

    private val _errorState: MutableStateFlow<AppState> = MutableStateFlow(AppState.Empty())
    val errorState = _errorState.asStateFlow()
    private val _authState: MutableStateFlow<AppState> = MutableStateFlow(AppState.Empty())
    val authState = _authState.asStateFlow()
    private val _isStreamingState = MutableStateFlow(false)
    val isStreamingState = _isStreamingState.asStateFlow()

    val loggedInState: StateFlow<Boolean?> =
        userDataStore.userData.map {
            it?.let { true } ?: false
        }.stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = null)

    override val externalRooms: StateFlow<List<RoomData>> =
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

    private val _watchedRoom = MutableStateFlow<WatchedRoom?>(null)
    override val watchedRoom = _watchedRoom.asStateFlow()
    private val _transmittingRoomCode = MutableStateFlow<String?>(null)
    val transmittingRoomCode = _transmittingRoomCode.asStateFlow()


    init {
        val context: Context = this.getApplication()
        val intent = Intent(context, CameraServiceImpl::class.java)
        context.startForegroundService(intent)
        context.bindService(intent, cameraServiceConnection, Context.BIND_AUTO_CREATE)
        fetchMyRooms()
        Log.i(TAG, "service bind")
        viewModelScope.launch {
            cameraService.collect {
                it?.serviceState?.collect { cameraServiceState ->
                    _isStreamingState.emit(cameraServiceState.isStreaming)
                    _errorState.emit(cameraServiceState.streamError)
                    _transmittingRoomCode.emit(cameraServiceState.streamName)
                }
            }
        }
    }

    private fun getRoom(code: String): RoomData? {
        return externalRooms.value.firstOrNull { it.code == code }
    }

    fun watch(code: String) {
        Log.i(TAG, "watch: watch 1")
        viewModelScope.launch {
            val room = getRoom(code)
            roomApiService.watchRoom(
                WatchRequest(code, room?.password)
            ).onSuccessWithErrorHandling {
                _watchedRoom.emit(it)
            }
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

    fun startStream(roomCamera: RoomCamera) {
        withCameraService {
            it.startStream(
                url = "${getApplication<Application>().getString(R.string.stream_url)}/${roomCamera.token}",
                name = roomCamera.code
            )
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

    override fun addExternalRoom(code: String, password: String) {
        viewModelScope.launch {
            val watchRoomResponse: kotlin.Result<WatchedRoom> = roomApiService.watchRoom(
                WatchRequest(code, password)
            )
            watchRoomResponse.onSuccessWithErrorHandling {
                roomDataStore.add(RoomData(code, password))
            }
        }
    }

    override fun removeExternalRoom(code: String) {
        viewModelScope.launch {
            roomDataStore.delete(code)
        }
    }

    override fun removeRoomCamera(roomCamera: RoomCamera) {
        viewModelScope.launch {
            roomCameraDataStore.delete(roomCamera)
        }
    }

    override fun addRoomCamera(
        cameraName: String,
        roomCode: String,
        password: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _authState.emit(AppState.Loading())
            var response =
                cameraApiService.addCamera(CameraRequest(roomCode, password, cameraName))
            response.onSuccessWithErrorHandling {
                val roomCamera = RoomCamera(
                    code = roomCode,
                    name = cameraName,
                    token = it.cameraToken,
                    url = it.cameraUrl,
                    roomType = RoomType.EXTERNAL
                )
                roomCameraDataStore.add(roomCamera)
                onSuccess()
                _authState.emit(AppState.Empty())
            }
            response.onFailure {
                _authState.emit(AppState.Empty())
            }
        }
    }

    fun emitError(error: AppState) {
        viewModelScope.launch {
            _errorState.emit(error)
        }
    }

    private suspend inline fun <T> Result<T>.onSuccessWithErrorHandling(block: (T) -> Unit = {}) {
        onSuccess {
            block(it)
        }
        onFailure {
            Log.i(TAG, "onSuccessWithErrorHandling: handling failure")
            _errorState.emit(AppState.Error("Something went wrong"))
        }
    }

}