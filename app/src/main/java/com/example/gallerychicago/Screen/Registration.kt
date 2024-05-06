package com.example.gallerychicago.Screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.sp
import java.util.Calendar
import android.app.DatePickerDialog
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gallerychicago.Data.ArtworkViewModel
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.livedata.observeAsState
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegistrationScreen(userViewModel: ArtworkViewModel = viewModel()) {
    val emailAvailable by userViewModel.emailAvailable.observeAsState()

    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        DisplayDatePicker { selectedDate ->
            dateOfBirth = selectedDate
            showDatePicker = false
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8D8C4))
    ){
        Box(  //for red background
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp) // height
                .background(Color(0xFF952323))
        ) {
            Text(//title
                "Register",
                color = Color(255,255,255),
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                ),
                modifier = Modifier.align(Alignment.Center) //
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") }
            )
            Spacer(modifier = Modifier.height(15.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") }
            )
            Spacer(modifier = Modifier.height(15.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(15.dp))
            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") } ,
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(15.dp))
            // Date of Birth TextField that triggers DatePickerDialog
            TextField(
                value = dateOfBirth,
                onValueChange = {},
                label = { Text("Date of Birth") },
                readOnly = true,
                modifier = Modifier.clickable { showDatePicker = true }
            )

            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = { showDatePicker = true },
                colors = ButtonDefaults.buttonColors(Color(0xFF952323)),
                modifier = Modifier.width(200.dp))
            {
                Text("Enter your birthday")
            }
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = {
                    if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank() || dateOfBirth.isBlank()) {
                        Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_LONG).show()
                    } else if (password != confirmPassword) {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_LONG).show()
                    } else {
                        userViewModel.addUser(name = username,email = email, password = password, birthday = dateOfBirth)
                    }
                },
                colors = ButtonDefaults.buttonColors(Color(0xFF952323)),
                modifier = Modifier.width(200.dp)
            ) {
                Text("Register")
            }

            LaunchedEffect(emailAvailable) {
                when (emailAvailable) {
                    true -> Toast.makeText(context, "Registration successful", Toast.LENGTH_LONG).show()
                    false -> Toast.makeText(context, "Email already exists", Toast.LENGTH_LONG).show()
                    null -> {} // No action needed
                }
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayDatePicker(onDateSelected: (String) -> Unit) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli()
    )
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)

    DatePickerDialog(
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(formatter.format(Date(datePickerState.selectedDateMillis!!)))
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = {onDateSelected("")}) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}



