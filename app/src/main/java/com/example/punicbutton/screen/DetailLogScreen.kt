package com.example.punicbutton.screen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.punicbutton.R
import com.example.punicbutton.viewmodel.LogDetailItem
import com.example.punicbutton.viewmodel.extractLogData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun DetailLogScreen(nomorRumah: String, context: Context) {
    var logData by remember { mutableStateOf<List<LogDetailItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(nomorRumah) {
        try {
            withContext(Dispatchers.IO) {
                val serverIp = context.getString(R.string.ipAdd)
                val url = URL("http://$serverIp/button/detail_log.php?log=$nomorRumah")
                val connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                connection.requestMethod = "GET"

                val inputStream = connection.inputStream
                val htmlData = inputStream.bufferedReader().use { it.readText() }
                println("Received Data: $htmlData")

                val document: Document = Jsoup.parse(htmlData)

                val errorElement: Element? = document.selectFirst("p")
                if (errorElement != null) {
                    errorMessage = errorElement.text()
                    logData = emptyList()
                } else {
                    val rows: List<Element> = document.select("table tr")
                    val extractedData = mutableListOf<LogDetailItem>()

                    for (i in 1 until rows.size) {
                        val columns = rows[i].select("td")
                        if (columns.size == 3) {
                            val waktu = columns[0].text()
                            val nomorRumah = columns[1].text()
                            val status = columns[2].text()
                            extractedData.add(LogDetailItem(waktu, nomorRumah, status))
                        }
                    }

                    logData = extractedData
                    errorMessage = null
                }
            }
        } catch (e: Exception) {
            errorMessage = "Exception: ${e.message}"
            logData = emptyList()
        } finally {
            isLoading = false
        }
    }

    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
    } else if (errorMessage != null) {
        Text("Error: $errorMessage", color = androidx.compose.ui.graphics.Color.Red, modifier = Modifier.padding(16.dp))
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(logData) { item ->
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = "Waktu: ${item.waktu}")
                    Text(text = "Nomor Rumah: ${item.nomorRumah}")
                    Text(text = "Status: ${item.status}")
                }
            }
        }
    }
}