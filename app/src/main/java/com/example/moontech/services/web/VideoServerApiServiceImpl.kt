package com.example.moontech.services.web

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import com.example.moontech.data.dataclasses.RecordRequest
import com.example.moontech.data.dataclasses.Recording
import com.example.moontech.data.dataclasses.StreamRequest
import com.example.moontech.data.dataclasses.StreamResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.utils.io.jvm.javaio.copyTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideoServerApiServiceImpl(private val httpClient: HttpClient, private val context: Context) :
    VideoServerApiService {
    private val collection =
        MediaStore.Video.Media.getContentUri(
            MediaStore.VOLUME_EXTERNAL
        )

    companion object {
        private const val endpoint = "/videoServer"
    }

    override suspend fun stream(streamRequest: StreamRequest): Result<StreamResponse> {
        return httpClient.postResult("$endpoint/streamUrl") {
            setBody(streamRequest)
        }
    }

    override suspend fun startRecord(request: RecordRequest): Result<Boolean> {
        return httpClient.putWithStatus("$endpoint/record/start") {
            setBody(request)
        }
    }

    override suspend fun stopRecord(request: RecordRequest, filePrefix: String): Result<Boolean> {
        return httpClient.putWithStatus("$endpoint/record/stop") {
            setBody(request)
        }
    }

    override suspend fun downloadRecording(recording: Recording): Result<String> {
        val response = httpClient.get("$endpoint/record/${recording.name}")
        return try {
            Result.success(downloadQ(response, recording.name))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun checkRecord(request: RecordRequest): Result<Boolean> {
        val response = httpClient.get("$endpoint/record/check") {
            parameter("id", request.cameraId)
        }
        return if (response.status.isSuccess()) {
            Result.success(!response.bodyAsText().toBoolean())
        } else {
            Result.failure(IllegalStateException("Record check failed"))
        }
    }

    override suspend fun deleteRecording(recording: Recording): Result<Boolean> {
        return httpClient.deleteWithStatus("$endpoint/record/${recording.name}")
    }

    private suspend fun downloadQ(response: HttpResponse, fileName: String): String =
        withContext(Dispatchers.IO) {
            if (response.status.isSuccess()) {
                val values = ContentValues().apply {
                    put(MediaStore.Video.Media.DISPLAY_NAME, response.headers["filename"])
                    put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/ConferenceVideos")
                    put(MediaStore.Video.Media.MIME_TYPE, "video/x-flv")
                    put(MediaStore.Video.Media.IS_PENDING, 1)
                }

                val resolver = context.contentResolver
                val uri = resolver.insert(collection, values)

                uri?.let {
                    resolver.openOutputStream(uri)?.use { outputStream ->
                        response.bodyAsChannel().copyTo(outputStream)
                    }
                    values.clear()
                    values.put(MediaStore.Video.Media.IS_PENDING, 0)
                    resolver.update(uri, values, null, null)
                } ?: throw RuntimeException("MediaStore failed for some reason")
                fileName
            } else {
                throw RuntimeException("OkHttp failed for some reason")
            }
        }
}