package com.example.gallerychicago

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ReportScreen(navController: NavHostController)
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8D8C4))
    ) {
        Box(  //for red background
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp) // height
                .background(Color(0xFF952323))
        ) {
            Text(//title
                "Report",
                color = Color(255, 255, 255),
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                ),
                modifier = Modifier.align(Alignment.Center) //
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
                "Dear tourist, " +
                        "the category you collected the most last week was Print, accounting for 40% of your collections."
            )
            Spacer(modifier = Modifier.height(20.dp))
            SimplifiedPieChart(data = simplifiedPieChartData)
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
                // 扇形中心的偏移位置
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