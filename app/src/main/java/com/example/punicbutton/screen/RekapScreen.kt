package com.example.punicbutton.screen


import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.punicbutton.R
import com.example.punicbutton.viewmodel.RekapItem
import com.example.punicbutton.viewmodel.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL


@Composable
fun RekapScreen(navController: NavController, context: Context, viewModel: ViewModel = ViewModel()) {
    var rekapData by remember { mutableStateOf(listOf<RekapItem>()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        while (true) {
            try {
                withContext(Dispatchers.IO) {
                    val serverIp = context.getString(R.string.ipAdd)
                    val url = URL("http://$serverIp/button/rekap.php")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.connectTimeout = 5000
                    connection.readTimeout = 5000
                    connection.requestMethod = "GET"

                    val inputStream = connection.inputStream
                    val data = inputStream.bufferedReader().use { it.readText() }
                    val extractedData = viewModel.extractRekapData(data)
                    rekapData = extractedData.take(5)
                    errorMessage = null
                }
            } catch (e: Exception) {
                errorMessage = e.message
                rekapData = emptyList()
            } finally {
                isLoading = false
            }
            delay(5000)
        }
    }

    Column {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else if (errorMessage != null) {
            Text("Error: $errorMessage", color = Color.Red, modifier = Modifier.padding(16.dp))
        } else {
            Text("Data Rekap", style = MaterialTheme.typography.h5, modifier = Modifier.padding(16.dp))
            LazyColumn {
                items(rekapData) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                navController.navigate("detail_log/${item.nomorRumah}")
                                Log.d("Navigation", "Navigating to detail_log/${item.nomorRumah}")
                            },
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(item.waktu)
                        Text(item.nomorRumah)
                        Text(item.status)
                    }
                    Divider()
                }
            }
        }
        TextButton(onClick = { navController.navigate("data_rekap") }) {
            Text("Lihat Selengkapnya")
        }
    }
}



