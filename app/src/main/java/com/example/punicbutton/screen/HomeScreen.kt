package com.example.punicbutton.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.punicbutton.viewmodel.LoginViewModel
import com.example.punicbutton.viewmodel.PanicButton

@Composable
fun HomeScreen(
    board: Int,
    snackbarHostState: SnackbarHostState,
    viewModel: PanicButton = viewModel()
) {
    var isOn by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current



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
        Switch(
            checked = isOn ,
            onCheckedChange = { checked ->
                isOn = checked
                isLoading = true
                viewModel.toggleDevice(isOn, board, snackbarHostState) {
                    isLoading = false
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
            enabled = !isLoading)
    }
}

@Preview(showBackground = true)
@Composable
private fun liat() {
    HomeScreen(board = 1, snackbarHostState = SnackbarHostState())
}