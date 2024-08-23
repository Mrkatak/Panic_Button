package com.example.punicbutton.allclass

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
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
        if (nomorRumah.isBlank() || sandi.isBlank()){
            Toast.makeText(context, "Pendaftaran gagal: Lengkapi data diatas", Toast.LENGTH_SHORT).show()
            return
        }


        viewModelScope.launch {
            val url = "http://172.16.100.130/button/login.php"
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

                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    Handler(Looper.getMainLooper()).post {
                        if (response.isSuccessful) {
                            val responseBody = response.body?.string()
                            if (responseBody?.contains("success") == true) {
                                Toast.makeText(context, "Login berhasil!", Toast.LENGTH_SHORT).show()
                                navController.navigate("home/$nomorRumah") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                Toast.makeText(context, "Login gagal: Nomor rumah atau sandi salah", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Login gagal: ${response.message}", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            })
        }
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
            val url = "http://172.16.100.130/button/registrasi.php"
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