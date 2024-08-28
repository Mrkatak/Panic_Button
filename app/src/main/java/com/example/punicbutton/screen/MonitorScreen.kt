package com.example.punicbutton.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.punicbutton.viewmodel.MonitorData
import com.example.punicbutton.viewmodel.fetchMonitorData
import kotlinx.coroutines.delay

@Composable
fun MonitorScreen() {
    var monitorData by remember { mutableStateOf<MonitorData?>(null) }

    LaunchedEffect(Unit) {
        while (true) {
            monitorData = fetchMonitorData() // This will now be safe and won't block the main thread
            delay(1000L) // Refresh every second
        }
    }

    if (monitorData != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Nomor Rumah",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = monitorData!!.nomorRumah,
                fontSize = 32.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = monitorData!!.waktu,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )
            RekapScreen()
        }
    } else {
        CircularProgressIndicator()
    }
}