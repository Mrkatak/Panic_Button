package com.example.punicbutton.screen

import android.util.Log
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun RekapScreen() {
    var rekapData by remember { mutableStateOf(listOf<RekapItem>()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch data from the web page periodically
    LaunchedEffect(Unit) {
        while (true) {
            try {
                withContext(Dispatchers.IO) {
                    val url = URL("http://172.16.100.128/button/rekap.php")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.connectTimeout = 5000
                    connection.readTimeout = 5000
                    connection.requestMethod = "GET"

                    val inputStream = connection.inputStream
                    val data = inputStream.bufferedReader().use { it.readText() }

                    // Process the HTML content to extract the table data
                    val extractedData = extractRekapData(data)
                    rekapData = extractedData.take(5)  // Only take the 5 most recent data entries
                    errorMessage = null
                }
            } catch (e: Exception) {
                errorMessage = e.message
                rekapData = emptyList()
            } finally {
                isLoading = false
            }
            delay(5000) // Delay for 10 seconds before fetching the data again
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
                            .padding(8.dp),
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
    }
}

// Function to extract data from HTML
fun extractRekapData(html: String): List<RekapItem> {
    val rekapItems = mutableListOf<RekapItem>()
    try {
        val document = Jsoup.parse(html)
        val rows = document.select("table tr")

        for (i in 1 until rows.size) { // Skip the header row
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

data class RekapItem(val waktu: String, val nomorRumah: String, val status: String)
