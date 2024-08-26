package com.example.punicbutton.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Callback
import okhttp3.Call
import okhttp3.Response
import java.io.IOException

@Composable
fun HomeScreenContent(board: Int, snackbarHostState: SnackbarHostState) {
    var isOn by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp)
                    .padding(bottom = 16.dp)
            )
        }

        Button(
            onClick = {
                isOn = !isOn
                isLoading = true
                toggleDevice(isOn, board, snackbarHostState, {
                    isLoading = false
                }, {
                    isLoading = false
                })
            },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.size(100.dp, 80.dp),
            enabled = !isLoading
        ) {
            Text(if (isOn) "Turn Off" else "Turn On")
        }
    }
}



private fun toggleDevice(on: Boolean, board: Int, snackbarHostState: SnackbarHostState, onLoadingChange: (Boolean) -> Unit, onComplete: () -> Unit) {
    val client = OkHttpClient()
    val state = if (on) 1 else 0
    val url = "http://172.16.100.135/button/esp_iot/proses.php?id=2&state=$state"

    val request = Request.Builder()
        .url(url)
        .build()

    onLoadingChange(true)

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            Log.e("ToggleDevice", "Request Failed: ${e.message}")
            CoroutineScope(Dispatchers.Main).launch {
                snackbarHostState.showSnackbar("Error: ${e.message}")
                onLoadingChange(false)  // Set loading to false
                onComplete()
            }
        }

        override fun onResponse(call: Call, response: Response) {
            CoroutineScope(Dispatchers.Main).launch {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    Log.d("ToggleDevice", "Response: $responseData")
                    snackbarHostState.showSnackbar("Device toggled successfully")
                } else {
                    Log.e("ToggleDevice", "Request Failed: ${response.message}")
                    snackbarHostState.showSnackbar("Failed to toggle device: ${response.message}")
                }
                onLoadingChange(false)
                onComplete()
            }
        }
    })
}
//test push















