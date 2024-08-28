package com.example.punicbutton.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.punicbutton.viewmodel.PanicButton

@Composable
fun HomeScreenContent(board: Int, snackbarHostState: SnackbarHostState, viewModel: PanicButton = viewModel()) {
    var isOn by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp)
                    .padding(bottom = 16.dp)
            )
        }

        Text(
            text = "Panic Button",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.padding(vertical = 24.dp))

        Button(
            onClick = {
                showDialog = true

            },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.size(100.dp, 80.dp),
            enabled = !isLoading

        ) {
            Text(if (isOn) "Turn Off" else "Turn On")

        }
    }

    // Confirmation dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirm Action") },
            text = { Text(text = "Are you sure you want to ${if (isOn) "turn off" else "turn on"} the alarm?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        isOn = !isOn
                        isLoading = true
                        viewModel.toggleDevice(isOn, board, snackbarHostState) {
                            isLoading = false
                        }
                        if (isOn) {
                            Toast.makeText(context, "Alarm Berbunyi", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Alarm Berhenti", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("No")
                }
            }
        )
    }
}





















