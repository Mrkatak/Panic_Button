package com.example.punicbutton.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.punicbutton.R
import com.example.punicbutton.viewmodel.PanicButton
import com.example.punicbutton.viewmodel.ViewModel
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    board: Int,
    snackbarHostState: SnackbarHostState,
    viewModel: PanicButton = viewModel(),
    navController: NavController
) {
    var isOn by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showKeluarDialog by remember { mutableStateOf(false) }
    val loginViewModel = ViewModel()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 100.dp, end = 24.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Button(
                onClick = { showKeluarDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.biru),
                    contentColor = Color.White
                )
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    painter = painterResource(id = R.drawable.ic_logout),
                    contentDescription = "ic_logout",
                    tint = Color.Black)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Keluar",
                    color = Color.Black)
            }
        }
        Text(
            text = "Panic Button",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
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
            Switch(
                checked = isOn,
                onCheckedChange = { checked ->
                    if (checked) {
                        showDialog = true
                    } else {
                        isOn = false
                        isLoading = true
                        viewModel.toggleDevice(isOn, board, snackbarHostState) {
                            isLoading = false
                        }
                    }
                },
                thumbContent = {
                    if (isOn) {
                        Icon(
                            painter = painterResource(id = R.drawable.onmode),
                            contentDescription = "on mode",
                            modifier = Modifier
                                .padding(5.dp),
                            tint = Color.Black
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.offmode),
                            contentDescription = "off mode",
                            modifier = Modifier
                                .padding(5.dp)
                                .size(24.dp),
                            tint = Color.White
                        )
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = colorResource(id = R.color.pudar),
                    uncheckedTrackColor = colorResource(id = R.color.merah_pudar),
                    uncheckedBorderColor = colorResource(id = R.color.merah),
                    checkedThumbColor = colorResource(id = R.color.biru),
                    uncheckedThumbColor = colorResource(id = R.color.merah),
                    checkedBorderColor = colorResource(id = R.color.biru)
                ),
                modifier = Modifier
                    .scale(1.8f)
                    .padding(20.dp),
                enabled = !isLoading
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Konfirmasi") },
            text = { Text("Apakah Anda yakin ingin mengaktifkan Panic Button?") },
            confirmButton = {
                Button(
                    onClick = {
                        isOn = true
                        isLoading = true
                        showDialog = false
                        viewModel.toggleDevice(isOn, board, snackbarHostState) {
                            isLoading = false
                        }
                    }
                ) {
                    Text("Ya")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Tidak")
                }
            }
        )
    }
    LaunchedEffect(key1 = isOn) {
        if (isOn) {
            delay(15000)
            isOn = false
            isLoading = true
            viewModel.toggleDevice(isOn, board, snackbarHostState){
                isLoading = false
            }
        }
    }
    if (showKeluarDialog) {
        AlertDialog(
            onDismissRequest = { showKeluarDialog},
            title = { Text( "Konfirmasi")},
            text = { Text("Apakah Anda yakin ingin keluar?")},
            confirmButton = {
                Button(
                    onClick = {
                        showKeluarDialog = false
                        loginViewModel.logout(context, navController)
                    }
                ) { Text("Ya")

            }
            },
            dismissButton = {
                Button(onClick = { showKeluarDialog = false }) {
                    Text("Tidak")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun liat() {
    HomeScreen(
        board = 1,
        snackbarHostState = SnackbarHostState(),
        navController = rememberNavController()
    )
}
