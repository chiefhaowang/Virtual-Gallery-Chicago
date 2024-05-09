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
import com.example.gallerychicago.Data.UserViewModel
import com.example.gallerychicago.firebaseInterface.CloudInterface
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate


@Composable
fun ReportScreen(navController: NavHostController, userViewModel: UserViewModel)
{
    val currentUser by userViewModel.currentUser.observeAsState()
    val email = currentUser?.email

    val pieEntries = remember { mutableStateOf<List<PieEntry>>(emptyList()) }

    DisposableEffect(Unit) {
        val cloudInterface = CloudInterface()
        if (email != null) {
            cloudInterface.readUserFavourite(email) { entries ->
                if (entries != null) {
                    pieEntries.value = entries

                }
            }
        }

        onDispose {

        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8D8C4))
    ) {
        Box(  //for red background
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF952323))
        ) {
            Text(
                text = "Report",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primary)
            )
        }
        // calculated the how much percentage of stared increased or decreased compared to the last week
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            Spacer(modifier = Modifier.height(40.dp))

            CenteredBlackText(
                "Favourite Artwork Types Category",
            )
            Spacer(modifier = Modifier.height(20.dp))

//            SimplifiedPieChart(data = simplifiedPieChartData)
            if (email != null) {

                if (pieEntries.value.isEmpty()) {
                    CircularProgressIndicator()
                } else {
                    val pieDataSet = PieDataSet(pieEntries.value, "Pie Data Set").apply {
                        colors = ColorTemplate.COLORFUL_COLORS.toList()
                        xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
                        yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
                        valueFormatter = PercentValueFormatter()
                        valueTextSize = 14f
                    }

                    val pieData = PieData(pieDataSet)

                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { context ->
                            PieChart(context).apply {
                                data = pieData
                                description.isEnabled = false
                                centerText = "Favourites"
                                setDrawCenterText(true)
                                setEntryLabelTextSize(20f)
                                setCenterTextSize(19f)
                                animateY(2000)
                            }
                        }
                    )
                }

            }
            Spacer(modifier = Modifier.height(30.dp))
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            CenteredBlackText("City landscape is your favorite one this week")
            Spacer(modifier = Modifier.height(30.dp))
            Image(
                painter = painterResource(id = R.drawable.gallery),
                contentDescription = "Joan Mitchell Artwork",
                modifier = Modifier
                    .height(200.dp) // set the photo height
                    .width(200.dp), // set the width
                alignment = Alignment.Center
            )
            Spacer(modifier = Modifier.height(50.dp))
            CenteredBlackText("Monet is your favorite artist this week")
        }
    }
}

@Composable
fun CenteredBlackText(text: String) {
    Text(
        text = text,
        color = Color(10, 10, 10),
        style = TextStyle(
            fontFamily = FontFamily.Serif,
            fontSize = 15.sp
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    )
}


//define the data structure of each item in pie chart
data class PieChartData(val category: String, val percentage: Float, val color: Color)

val simplifiedPieChartData = listOf(
    PieChartData("Painting", 30f, Color(0xFF003366).copy(alpha = 0.9f)),
    PieChartData("Vessel", 30f, Color(0xFFF28705).copy(alpha = 0.9f)),
    PieChartData("Print", 40f, Color(0xFF8B0000).copy(alpha = 0.9f))
)

//Pie Chart
@Composable
fun SimplifiedPieChart(data: List<PieChartData>) {
    Box(modifier = Modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(150.dp)) {
            val total = 100f // 100%
            var startAngle = 0f
            val radius = size.minDimension / 2
            val center = Offset(size.width / 2, size.height / 2)

            data.forEach { chartData ->
                val sweepAngle = (chartData.percentage / total) * 360f
                //
                val sectionCenterAngle = Math.toRadians((startAngle + sweepAngle / 2).toDouble()).toFloat()
                val sectionCenterOffset = Offset(
                    cos(sectionCenterAngle) * radius * 0.5f,
                    sin(sectionCenterAngle) * radius * 0.5f
                )
                drawArc(
                    color = chartData.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    size = Size(radius * 2, radius * 2),
                    topLeft = Offset(center.x - radius, center.y - radius)
                )
                // 绘制文本标签
                withTransform({
                    translate(left = center.x + sectionCenterOffset.x, top = center.y + sectionCenterOffset.y)
                }) {
                    val percentageText = chartData.category + " ${chartData.percentage.toInt()}%"
                    drawContext.canvas.nativeCanvas.drawText(
                        percentageText,
                        //chartData.category,
                        0f,
                        0f,
                        Paint().apply {
                            textSize = 25f
                            color = android.graphics.Color.WHITE
                            textAlign = Paint.Align.CENTER
                        }
                    )
                }
                startAngle += sweepAngle
            }
        }
    }
}

//@Composable
//fun PieChartScreen(email: String) {
//    val pieEntries = remember { mutableStateOf<List<PieEntry>>(emptyList()) }
//
//    DisposableEffect(Unit) {
//        val cloudInterface = CloudInterface()
//        cloudInterface.readUserFavourite(email) { entries ->
//            if (entries != null) {
//                pieEntries.value = entries
//
//            }
//        }
//
//        onDispose {
//
//        }
//    }
//
//    if (pieEntries.value.isEmpty()) {
//        CircularProgressIndicator()
//    } else {
//        val pieDataSet = PieDataSet(pieEntries.value, "Pie Data Set").apply {
//            colors = ColorTemplate.COLORFUL_COLORS.toList()
//            xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
//            yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
//            valueFormatter = PercentValueFormatter()
//            valueTextSize = 14f
//        }
//
//        val pieData = PieData(pieDataSet)
//
//        AndroidView(
//            modifier = Modifier.fillMaxSize(),
//            factory = { context ->
//                PieChart(context).apply {
//                    data = pieData
//                    description.isEnabled = false
//                    centerText = "Favourites"
//                    setDrawCenterText(true)
//                    setEntryLabelTextSize(20f)
//                    setCenterTextSize(19f)
//                    animateY(2000)
//                }
//            }
//        )
//    }
//}


//we used this class for formatting value (adding % sign)
class PercentValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
//you can create your own formatting style below
        return "${value.toInt()}%"
    }
}

