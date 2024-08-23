package com.example.punicbutton.allclass

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.punicbutton.api.LoginResponse
import com.example.punicbutton.api.RetrofitClient
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
    fun login(nomorRumah: String, sandi: String, callback: (LoginResponse) -> Unit) {
        viewModelScope.launch {
            val response = RetrofitClient.apiService.login(nomorRumah, sandi).execute()
            if (response.isSuccessful) {
                callback(response.body()!!)
            } else {
                callback(LoginResponse(false, "Login failed"))
            }
        }
    }
}

//class Register
class RegisterViewModel : ViewModel() {
    private val client = OkHttpClient()

    fun register(nomorRumah: String, sandi: String, context: Context, navController: NavController) {
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
                        viewModelScope.launch {
                            Toast.makeText(context, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show()
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