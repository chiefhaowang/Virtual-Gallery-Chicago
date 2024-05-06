package com.example.gallerychicago.Screen

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
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
fun UserProfile(navController: NavHostController)
{
    var showDialog by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("Arthur Song") }

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
                .height(100.dp) // height
                .background(Color(0xFF952323))
        ) {
            Text(
                "User Profile",
                color = Color(255,255,255),
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                modifier = Modifier.align(Alignment.Center) //
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
            // Username and Edit Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Username: $username",
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontSize = 19.sp,
                        color = Color.DarkGray,
                    ),
                    modifier = Modifier.padding(20.dp) //
                )
                Button(onClick = { showDialog = true},
                    colors = ButtonDefaults.buttonColors(Color(0xFF952323)))
                {
                    Text("Edit")
                }
                if (showDialog) {
                    EditDialog(
                        initialValue = username,
                        label = "Username",
                        onSave = { newName ->
                            username = newName // update user name
                            showDialog = false
                        },
                        onDismiss = { showDialog = false }
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            // email
            Text(
                text = "Email: Arth2983@student.monash.edu",
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
                text = "Birthday: 1st January, 2000",
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
        DescriptionOfUser()
        Spacer(modifier = Modifier.height(50.dp))
        Button(onClick = { },
            colors = ButtonDefaults.buttonColors(Color(0xFF952323)))
        {
            Text("Go to my favorite list")
        }
        // Navigation bottom
        Spacer(modifier = Modifier.weight(1f))
    }
}

// user description
@Composable
fun DescriptionOfUser() {
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
        Row(){
            // user description
            Text(
                text = "\"Art is the lie that enables us to realize the truth.\" — Pablo Picasso",
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Italic, // 斜体
                    color = Color(0xFF757575),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp, end = 15.dp)
            )
            Button(onClick = { },
                colors = ButtonDefaults.buttonColors(Color(0xFF952323)))
            {
                Text("Edit")
            }
        }

    }
}