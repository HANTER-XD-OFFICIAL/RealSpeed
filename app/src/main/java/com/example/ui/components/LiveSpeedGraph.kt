package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SpeedSample
import com.example.model.TestStage
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun LiveSpeedGraph(
    samples: List<SpeedSample>,
    currentSpeedMbps: Double,
    peakMbps: Double,
    testStage: TestStage,
    isBengali: Boolean = false,
    modifier: Modifier = Modifier
) {
    val isUploading = testStage == TestStage.UPLOADING || (samples.lastOrNull()?.isDownload == false && testStage != TestStage.DOWNLOADING)
    val isDownload = testStage == TestStage.DOWNLOADING || (samples.lastOrNull()?.isDownload == true)

    val lineColor = when {
        isUploading -> Color(0xFFC084FC) // Neon Amethyst for Uplink
        isDownload -> NeonCyan
        else -> ElectricBlue
    }

    val isRunning = testStage != TestStage.IDLE && testStage != TestStage.COMPLETED && testStage != TestStage.ERROR

    val infiniteTransition = rememberInfiniteTransition(label = "graphScan")
    val scanLineX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (isUploading) 1400 else 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scanner"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(125.dp)
            .background(CyberSurface, RoundedCornerShape(16.dp))
            .border(
                1.dp,
                if (isUploading) Color(0xFFC084FC).copy(alpha = 0.4f) else CyberCardBorder.copy(alpha = 0.7f),
                RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .testTag("live_speed_graph")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(if (isRunning) lineColor else TextMuted, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isUploading) "▲ Uplink Transmission Telemetry" else "Real-Time Telemetry Stream",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isUploading) Color(0xFFE2E8F0) else TextPrimary
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Peak Indicator Pill (Gigabit formatted)
                val peakText = if (peakMbps >= 1000.0) {
                    "${if (isUploading) "UP PEAK" else "PEAK"}: ${String.format("%.2f", peakMbps / 1000.0)} Gbps"
                } else {
                    "${if (isUploading) "UP PEAK" else "PEAK"}: ${String.format("%.1f", peakMbps)} Mbps"
                }

                Box(
                    modifier = Modifier
                        .background(lineColor.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                        .border(1.dp, lineColor.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = peakText,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Black,
                        color = lineColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height

                    // Gridlines
                    val gridColor = Color(0x14FFFFFF)
                    val dashedEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 10f), 0f)

                    drawLine(gridColor, Offset(0f, height * 0.25f), Offset(width, height * 0.25f), strokeWidth = 1.dp.toPx(), pathEffect = dashedEffect)
                    drawLine(gridColor, Offset(0f, height * 0.50f), Offset(width, height * 0.50f), strokeWidth = 1.dp.toPx(), pathEffect = dashedEffect)
                    drawLine(gridColor, Offset(0f, height * 0.75f), Offset(width, height * 0.75f), strokeWidth = 1.dp.toPx(), pathEffect = dashedEffect)

                    if (samples.size >= 2) {
                        val maxSpeed = (samples.maxOfOrNull { it.speedMbps } ?: 10.0).coerceAtLeast(5.0) * 1.15
                        val stepX = width / (samples.size - 1).coerceAtLeast(1)

                        val path = Path()
                        val fillPath = Path()

                        val firstY = (height - (samples[0].speedMbps / maxSpeed * height)).toFloat().coerceIn(0f, height)
                        path.moveTo(0f, firstY)
                        fillPath.moveTo(0f, height)
                        fillPath.lineTo(0f, firstY)

                        for (i in 1 until samples.size) {
                            val x = i * stepX
                            val y = (height - (samples[i].speedMbps / maxSpeed * height)).toFloat().coerceIn(0f, height)
                            val prevX = (i - 1) * stepX
                            val prevY = (height - (samples[i - 1].speedMbps / maxSpeed * height)).toFloat().coerceIn(0f, height)

                            val midX = (prevX + x) / 2f
                            path.cubicTo(midX, prevY, midX, y, x, y)
                            fillPath.cubicTo(midX, prevY, midX, y, x, y)
                        }

                        val lastX = (samples.size - 1) * stepX
                        fillPath.lineTo(lastX, height)
                        fillPath.close()

                        // Gradient Area Fill
                        val fillGradient = if (isUploading) {
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFC084FC).copy(alpha = 0.35f),
                                    Color(0xFF818CF8).copy(alpha = 0.15f),
                                    Color.Transparent
                                ),
                                startY = 0f,
                                endY = height
                            )
                        } else {
                            Brush.verticalGradient(
                                colors = listOf(lineColor.copy(alpha = 0.25f), Color.Transparent),
                                startY = 0f,
                                endY = height
                            )
                        }

                        drawPath(
                            path = fillPath,
                            brush = fillGradient
                        )

                        // Glowing Main Oscillograph Line
                        drawPath(
                            path = path,
                            color = lineColor,
                            style = Stroke(width = 2.4.dp.toPx(), cap = StrokeCap.Round)
                        )

                        // Live Head Tip Marker
                        val lastY = (height - (samples.last().speedMbps / maxSpeed * height)).toFloat().coerceIn(0f, height)
                        drawCircle(
                            color = lineColor.copy(alpha = 0.45f),
                            radius = 7.dp.toPx(),
                            center = Offset(lastX, lastY)
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 2.8.dp.toPx(),
                            center = Offset(lastX, lastY)
                        )
                    }

                    // Scanner Sweep Beam
                    if (isRunning) {
                        val scanX = width * scanLineX
                        drawLine(
                            brush = Brush.horizontalGradient(
                                listOf(Color.Transparent, lineColor.copy(alpha = 0.40f), Color.Transparent),
                                startX = scanX - 25f,
                                endX = scanX + 25f
                            ),
                            start = Offset(scanX, 0f),
                            end = Offset(scanX, height),
                            strokeWidth = 1.8.dp.toPx()
                        )
                    }
                }
            }
        }
    }
}
