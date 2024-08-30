package com.example.punicbutton.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.punicbutton.viewmodel.MonitorData
import com.example.punicbutton.viewmodel.ViewModel
import kotlinx.coroutines.delay

@Composable
fun MonitorScreen(viewModel: ViewModel = ViewModel()) {
    var monitorData by remember { mutableStateOf<MonitorData?>(null) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        while (true) {
            monitorData = viewModel.fetchMonitorData(context)
            delay(1000L)
        }
    }

    if (monitorData != null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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

        }
    } else {
        CircularProgressIndicator()
    }
}

