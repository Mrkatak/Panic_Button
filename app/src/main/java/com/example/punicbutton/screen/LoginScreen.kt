package com.example.punicbutton.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
import com.example.punicbutton.allclass.LoginViewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel()
) {
    val (nomorRumah, setNomorRumah) = remember { mutableStateOf("") }
    val (sandi, setSandi) = remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, end = 24.dp),
        contentAlignment = Alignment.Center
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
                text = "Masuk",
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
                viewModel.login(nomorRumah, sandi, context, navController)
            },
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Masuk")
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = {
                navController.navigate("register")
            }) {
                Text("Daftar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun liat() {
    val navController = rememberNavController()
    LoginScreen(navController = navController)

}



@Composable
fun ColumnWithShadow() {
    // Surface is used to apply shadow and other styles
    Surface(
        modifier = Modifier
            .padding(16.dp)
            .shadow(8.dp, RoundedCornerShape(8.dp)), // shadow with rounded corners
        shape = RoundedCornerShape(8.dp), // same rounded corner for content inside
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White)
        ) {
            // Add content to your column here
            Text("Item 1")
            Text("Item 2")
            Text("Item 3")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewColumnWithShadow() {
    ColumnWithShadow()
}
