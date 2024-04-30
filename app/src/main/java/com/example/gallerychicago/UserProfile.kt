package com.example.gallerychicago

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
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.navigation.NavHostController

@Composable
fun UserProfile(navController: NavHostController)
{
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
                    text = "Username: Arthur Song",
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontSize = 19.sp,
                        color = Color.DarkGray,
                    ),
                    modifier = Modifier.padding(20.dp) //
                )
                Button(onClick = { },
                    colors = ButtonDefaults.buttonColors(Color(0xFF952323)))
                {
                    Text("Edit")
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