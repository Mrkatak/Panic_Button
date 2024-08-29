package com.example.punicbutton.viewmodel

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.punicbutton.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

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

        val url = "http://${context.getString(R.string.ipAdd)}/button/login.php"
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

                            saveUserLogin(context, nomorRumah)

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

    private fun saveUserLogin(context: Context, nomorRumah: String) {
        val sharedPref = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("nomorRumah", nomorRumah)
            apply()
        }
    }

    fun checkUserLogin(context: Context, navController: NavController) {
        val sharedPref = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val nomorRumah = sharedPref.getString("nomorRumah", null)

        if (nomorRumah != null) {
            navController.navigate("home/$nomorRumah") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    fun logout(context: Context, navController: NavController) {
        val sharedPref = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove("nomorRumah")
            apply()
        }

        navController.navigate("login") {
            popUpTo("home") { inclusive = true }
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
            val url = "http://${context.getString(R.string.ipAdd)}/button/registrasi.php"
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
class PanicButton (application: Application) : AndroidViewModel(application) {
    private val client = OkHttpClient()

    fun toggleDevice(
        on: Boolean,
        board: Int,
        snackbarHostState: SnackbarHostState,
        onLoadingChange: (Boolean) -> Unit
    ) {
        val ipAddress = getApplication<Application>().getString(R.string.ipAdd)
        val client = OkHttpClient()
        val state = if (on) 1 else 0
        val url = "http://$ipAddress/button/esp_iot/proses.php?id=2&state=$state"

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


data class MonitorData(val nomorRumah: String, val waktu: String)
suspend fun fetchMonitorData(context: Context): MonitorData? {
    return withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://${context.getString(R.string.ipAdd)}/button/monitor.php")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return@withContext null

            val html = response.body?.string() ?: return@withContext null
            val document: Document = Jsoup.parse(html)
            val nomorRumah = document.select("#logTable td").first()?.text()
            val waktu = document.select("#logTable td").get(1).text()

            return@withContext MonitorData(nomorRumah.toString(), waktu)
        }
    }
}


data class RekapItem(val waktu: String, val nomorRumah: String, val status: String)
fun extractRekapData(html: String): List<RekapItem> {
    val rekapItems = mutableListOf<RekapItem>()
    try {
        val document = Jsoup.parse(html)
        val rows = document.select("table tr")

        for (i in 1 until rows.size) {
            val cols = rows[i].select("td")
            if (cols.size >= 3) {
                val waktu = cols[0].text()
                val nomorRumah = cols[1].text()
                val status = cols[2].text()
                rekapItems.add(RekapItem(waktu, nomorRumah, status))
            }
        }
    } catch (e: Exception) {
        Log.e("RekapScreen", "Error parsing HTML", e)
    }
    return rekapItems
}


data class LogDetailItem( val waktu: String, val nomorRumah: String, val status: String)
fun extractLogData(data: String): List<LogDetailItem> {
    val logList = mutableListOf<LogDetailItem>()

    try {
        val jsonArray = JSONArray(data)

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val waktu = jsonObject.getString("waktu")
            val nomorRumah = jsonObject.getString("nomorRumah")
            val status = jsonObject.getString("status")

            val logItem = LogDetailItem(waktu, nomorRumah, status)
            logList.add(logItem)
        }
    } catch (e: JSONException) {
        e.printStackTrace()
    }
    return logList
}





