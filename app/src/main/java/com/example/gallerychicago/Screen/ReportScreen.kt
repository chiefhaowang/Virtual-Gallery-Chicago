package com.example.gallerychicago.Screen

import android.annotation.SuppressLint
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gallerychicago.R
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberAsyncImagePainter
import com.example.gallerychicago.Data.UserViewModel
import com.example.gallerychicago.firebaseInterface.CloudInterface
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate


@Composable
fun ReportScreen(navController: NavHostController, userViewModel: UserViewModel) {
    val currentUser by userViewModel.currentUser.observeAsState()
    val email = currentUser?.email
    val pieEntries = remember { mutableStateOf<List<PieEntry>>(emptyList()) }

    LaunchedEffect(email) {
        if (email != null) {
            CloudInterface().readUserFavourite(email) { entries ->
                if (entries != null) {
                    pieEntries.value = entries
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFE8D8C4)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().background(Color(0xFF952323)),
        ) {
            Text(
                text = "Reports",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().background(color = MaterialTheme.colorScheme.primary)
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface),
        ) {
            CenteredBlackText("My favourite Artwork Category")

            if (email != null && pieEntries.value.isNotEmpty()) {
                PieChartComponent(pieEntries.value)
            } else {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(30.dp))
            }
        }

//        Column(
//            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface),
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            CenteredBlackText("Most Favoured Artwork")
//
//            Text(
//                text = "Improvisation No. 30 (Cannons)",
//                color = Color(10, 10, 10),
//                style = TextStyle(
//                    fontFamily = FontFamily.Serif,
//                    fontSize = 15.sp,
//                    textAlign = TextAlign.Center
//                )
//            )
//
//            Image(
//                painter = rememberAsyncImagePainter(
//                    model = "https://www.artic.edu/iiif/2/9e00e226-9264-cda0-2893-685e0ca5b285/full/200,/0/default.jpg"
//                ),
//                contentDescription = "Improvisation No. 30 (Cannons)",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(400.dp)
//                    .padding(16.dp)
//            )
//        }
    }
}

@Composable
fun PieChartComponent(entries: List<PieEntry>) {
    val pieDataSet = PieDataSet(entries, "Pie Data Set").apply {
        colors = ColorTemplate.COLORFUL_COLORS.toList()
        xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
        yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
        valueFormatter = PercentValueFormatter()
        valueTextSize = 40f
    }
    val pieData = PieData(pieDataSet)
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(370.dp),
        factory = { context ->
            PieChart(context).apply {
                data = pieData
                description.isEnabled = false
                centerText = "Favourites"
                setDrawCenterText(true)
                setEntryLabelTextSize(14f)
                animateY(2000)
            }
        }
    )
}

@Composable
fun CenteredBlackText(text: String) {
    Text(
        text = text,
        color = Color(10, 10, 10),
        style = TextStyle(
            fontFamily = FontFamily.Serif,
            fontSize = 20.sp
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(vertical = 20.dp)
    )
}


//we used this class for formatting value (adding % sign)
class PercentValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
//you can create your own formatting style below
        return "${value.toInt()}%"
    }
}

