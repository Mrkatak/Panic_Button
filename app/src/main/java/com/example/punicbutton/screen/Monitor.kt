package com.example.punicbutton.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.punicbutton.viewmodel.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.punicbutton.R
import com.example.punicbutton.viewmodel.MonitorData
import kotlinx.coroutines.delay

@Composable
fun ScreenMonitor(modifier: Modifier = Modifier, viewModel: ViewModel = viewModel()) {
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
        modifier
            .padding(start = 24.dp, end = 24.dp, top = 46.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier
                .fillMaxWidth()
                .background(color = Color.Red, RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Informasi Darurat",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Column(
            modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.biru), RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Nomor Rumah",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = monitorData!!.nomorRumah,
                fontSize = 46.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = monitorData!!.waktu,
                fontSize = 16.sp,
            )
        }
        }
    } else {
        CircularProgressIndicator()
    }
}
