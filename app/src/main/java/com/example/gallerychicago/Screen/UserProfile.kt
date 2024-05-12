package com.example.gallerychicago.Screen

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.gallerychicago.Data.UserViewModel
import com.example.gallerychicago.R


// logic to control edit
@Composable
fun EditDialog(
    initialValue: String,
    label: String,
    onSave: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf(initialValue) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("$label", style = MaterialTheme.typography.headlineSmall)
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(label) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { onSave(text) },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Save")
                }
                Button(onClick = { onDismiss() }) {
                    Text("Cancel")
                }
            }
        }
    }
}


@Composable
fun UserProfile(navController: NavHostController, userViewModel: UserViewModel = viewModel())
{
    val currentUser by userViewModel.currentUser.observeAsState()

    var showDialogUsername by remember { mutableStateOf(false) }
    var showDialogDescription by remember { mutableStateOf(false) }
    // UI displays name and description
    var displayUsername by remember { mutableStateOf("${currentUser?.name}") }
    var displayDescription by remember {mutableStateOf("${currentUser?.description}")}

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFE8D8C4)),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
    )

    {
        Box(  //for red background
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF952323))
        ) {
            Text(
                text = "User Profile",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primary)
            )
        }

        // User avatar
        Column(//horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(10.dp))
        {
            Spacer(modifier = Modifier.height(30.dp))
            // avatar
            Icon(
                painter = painterResource(id = R.drawable.icon_user_avatar),
                contentDescription = "icon_user",
                tint = Color(0xFF17273B),
                modifier = Modifier
                    .padding(30.dp, 10.dp)
                    .size(80.dp)
                    .align(Alignment.CenterHorizontally)
            )
            /**
             * Username and Edit Button
             */
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Username: ${currentUser?.name}",
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontSize = 19.sp,
                        color = Color.DarkGray,
                    ),
                    modifier = Modifier.padding(20.dp) //
                )
                Button(onClick = { showDialogUsername = true },
                    colors = ButtonDefaults.buttonColors(Color(0xFF952323)))
                {
                    Text("Edit")
                }
                if (showDialogUsername) {
                    EditDialog(
                        initialValue = currentUser?.name ?: "",
                        label = "Username",
                        onSave = { newName ->
                            displayUsername = newName
                            userViewModel.updateUserName(currentUser?.id ?: -1, newName)
                            showDialogUsername = false
                        },
                        onDismiss = { showDialogUsername = false }
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            // email
            Text(
                text = "Email: ${currentUser?.email}",
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 19.sp,
                    color = Color.DarkGray
                ),
                modifier = Modifier
                    .padding(start = 20.dp)
                    .align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Birthday: ${currentUser?.birthday}",
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 19.sp,
                    color = Color.DarkGray,
                ),
                modifier = Modifier
                    .padding(start = 20.dp)
                    .align(Alignment.Start)
            )
        }
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                // "Description"
                Text(
                    text = "Description",
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontSize = 20.sp,
                        color = Color.Black
                    ),
                    modifier = Modifier.padding(10.dp)
                )
            }
            /**
             * user description
             */
            Row(){
                Text(
                    text = "${currentUser?.description}",
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFF757575),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp, end = 15.dp)
                )
                Button(
                    onClick = { showDialogDescription = true },
                    colors = ButtonDefaults.buttonColors(Color(0xFF952323)))
                {
                    Text("Edit")
                }
                // if description is edited, update the database and ui
                if (showDialogDescription) {
                    EditDialog(
                        initialValue = currentUser?.description ?: "",
                        label = "Description",
                        onSave = { newDescription ->
                            displayDescription = newDescription
                            userViewModel.updateUserDescription(currentUser?.id ?: -1, newDescription)
                            showDialogDescription = false
                        },
                        onDismiss = { showDialogDescription = false }
                    )
                }
            }

        }
        Spacer(modifier = Modifier.height(50.dp))
        // Button to go Favourite list
        Button(
            onClick = {
                println("Button clicked!")
                navController.navigate("FavouriteList") },
            modifier = Modifier.width(200.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF952323)))
        {
            Text("Go to my favorite list")
        }

        Spacer(modifier = Modifier.height(10.dp))
        //Button to login out
        Button(onClick = {
            // This clears the back stack up to the 'loginScreen'
            navController.navigate("loginScreen")
            userViewModel.logoutUser()
        },
            modifier = Modifier.width(200.dp)
        ) {
            Text("Log Out")
        }

        // Button to login in
        Button(onClick = {
            // This clears the back stack up to the 'loginScreen'
            navController.navigate("loginScreen")

        }) {

            Text("Log in")
        }

        // Navigation bottom
        Spacer(modifier = Modifier.weight(1f))
    }
}
