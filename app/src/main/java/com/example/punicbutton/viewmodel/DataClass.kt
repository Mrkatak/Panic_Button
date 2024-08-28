package com.example.punicbutton.viewmodel

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.punicbutton.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException


// class Login
class LoginViewModel : ViewModel() {
    private val client = OkHttpClient()

    fun login(nomorRumah: String, sandi: String, context: Context, navController: NavController) {
        if (nomorRumah.isBlank() || sandi.isBlank()) {
            Toast.makeText(context, "MAsuk gagal: Lengkapi data di atas", Toast.LENGTH_SHORT).show()
            return
        }

        if (nomorRumah == "admin" && sandi == "admin") {
            Toast.makeText(context, "Login sebagai admin berhasil!", Toast.LENGTH_SHORT).show()
            navController.navigate("admin") {
                popUpTo("login") { inclusive = true }
            }
            return
        }


        val url = "http://172.16.100.128/button/login.php"
        val requestBody = FormBody.Builder()
            .add("nomor_rumah", nomorRumah)
            .add("sandi", sandi)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("LoginViewModel", "Error during login", e)
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(context, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string() ?: ""
                    Log.d("LoginViewModel", "Response Body: $responseBody")
                    Handler(Looper.getMainLooper()).post {
                        if (responseBody.contains("success")) {
                            Toast.makeText(context, "Login berhasil!", Toast.LENGTH_SHORT).show()
                            navController.navigate("home/$nomorRumah") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            Toast.makeText(context, "Login gagal: Nomor rumah atau sandi salah", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Login gagal: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}

//class Register
class RegisterViewModel : ViewModel() {
    private val client = OkHttpClient()

    fun register(nomorRumah: String, sandi: String, context: Context, navController: NavController) {
        if (nomorRumah.isBlank() || sandi.isBlank()){
            Toast.makeText(context, "Pendaftaran gagal: Lengkapi data diatas", Toast.LENGTH_SHORT).show()
            return
        }

        viewModelScope.launch {
            val url = "http://172.16.100.128/button/registrasi.php"
            val requestBody = FormBody.Builder()
                .add("nomor_rumah", nomorRumah)
                .add("sandi", sandi)
                .build()

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()

                    viewModelScope.launch {
                        Toast.makeText(context, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val launch = viewModelScope.launch {
                            Toast.makeText(context, "Pendaftaran berhasil!", Toast.LENGTH_SHORT)
                                .show()
                            navController.navigate("login") {
                                popUpTo("register") { inclusive = true }
                            }

                        }
                    } else {
                        viewModelScope.launch {
                            Toast.makeText(context, "Pendaftaran gagal: ${response.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }
    }
}

//class Panic Button
class PanicButton : ViewModel() {
    private val client = OkHttpClient()

    fun toggleDevice(
        on: Boolean,
        board: Int,
        snackbarHostState: SnackbarHostState,
        onLoadingChange: (Boolean) -> Unit
    ) {
        val client = OkHttpClient()
        val state = if (on) 1 else 0
        val url = "http://172.16.100.128/button/esp_iot/proses.php?id=2&state=$state"

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
                    onLoadingChange(false)

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

                }
            }
        })
    }
}





