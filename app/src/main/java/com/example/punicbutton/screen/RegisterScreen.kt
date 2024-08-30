package com.example.punicbutton.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.punicbutton.R
import com.example.punicbutton.viewmodel.ViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RegisterScreen(navController: NavController, viewModel: ViewModel = viewModel()) {
    val (nomorRumah, setNomorRumah) = remember { mutableStateOf("") }
    val (sandi, setSandi) = remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, end = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .shadow(16.dp, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .background(color = colorResource(id = R.color.pudar), RoundedCornerShape(16.dp))
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Daftar",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextField(
                    value = nomorRumah,
                    onValueChange = { setNomorRumah(it) },
                    label = { Text("Nomor Rumah") },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = sandi,
                    onValueChange = { setSandi(it) },
                    label = { Text("Sandi") },
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    viewModel.register(nomorRumah, sandi, context, navController)
                },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Daftar")
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = {
                    navController.popBackStack()
                }) {
                    Text("Kembali")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun liat() {
    val navController = rememberNavController()
    RegisterScreen(navController = navController)
}

