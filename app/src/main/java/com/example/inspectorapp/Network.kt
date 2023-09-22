package com.example.inspectorapp

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

internal fun CoroutineScope.callUrl(snackbarHostState: SnackbarHostState, url: String) {
    makeRequest(url) { rc, content ->
        val contentLength = content?.length ?: 0
        Log.i("NetworkApp", "Content: $contentLength")
        snackbarHostState.showSnackbar("RC=$rc Content=$contentLength")
    }
}

internal fun CoroutineScope.makeRequest(url: String, block: suspend (Int, String?) -> Unit) {
    launch(Dispatchers.IO) {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.setRequestProperty("Accept-Encoding", "gzip")
        val rc = connection.responseCode
        Log.i("NetworkApp", "Response: $rc")

        val content = when (rc) {
            200 -> connection.inputStream.reader().use { it.readText() }
            else -> null
        }
        block(rc, content)
    }
}
