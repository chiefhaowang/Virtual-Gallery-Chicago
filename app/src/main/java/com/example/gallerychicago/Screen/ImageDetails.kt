package com.example.gallerychicago.Screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gallerychicago.R


@Composable
fun Imagedetials() {

    Surface(color = MaterialTheme.colorScheme.surface) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Artwork Details",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primary)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
//            contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.gallery_welcome2),
                    contentDescription = "image details",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .border(
                            BorderStroke(1.dp, Color.White)
                        ),
                    contentScale = ContentScale.FillWidth
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Child on the Cliff",
                    fontSize = 30.sp,
                    fontFamily = FontFamily.Cursive,
                    color = Color.Black,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(10.dp, 0.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.icon_favourite),
                    contentDescription = "icon-favourite",
                    modifier = Modifier
                        .padding(start = 100.dp, end = 10.dp, top = 10.dp)
                        .size(20.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.icon_like),
                    contentDescription = "icon-favourite",
                    modifier = Modifier
                        .padding(start = 10.dp, end = 0.dp, top = 10.dp)
                        .size(20.dp)
                )
            }

            Text(
                text = "Time Added: year/month/day",
                modifier = Modifier.padding(20.dp, 0.dp),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                fontFamily = FontFamily.Serif
            )

            Text(
                text = "The show also takes a closer look at the visual alchemy that results from combining different raw materials and techniques, such as coloring materials prior to weaving and using contemporary technology to translate concepts to cloth.",
                modifier = Modifier.padding(20.dp, 20.dp),
                fontFamily = FontFamily.Serif,
                //color = colorResource(id = R.color.text_sub)
            )

        }

    }
//        Column(
//        modifier = Modifier
//            .background(color = colorResource(id = R.color.background))
//            .fillMaxHeight()
//
//    )
}