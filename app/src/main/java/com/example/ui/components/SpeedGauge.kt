package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TestStage
import com.example.ui.theme.CyberBlue
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.GaugeTrack
import com.example.ui.theme.GaugeTrackBorder
import com.example.ui.theme.GlowPink
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SpeedGauge(
    currentSpeedMbps: Double,
    testStage: TestStage,
    progress: Float,
    isBengali: Boolean = false,
    modifier: Modifier = Modifier
) {
    val isRunning = testStage != TestStage.IDLE && testStage != TestStage.COMPLETED && testStage != TestStage.ERROR

    // Calibrated logarithmic-like mapping for speed scale (0 to 1000+ Mbps Gigabit)
    val targetRatio = when {
        currentSpeedMbps <= 0.0 -> 0f
        currentSpeedMbps <= 1.0 -> (currentSpeedMbps / 1.0 * 0.10).toFloat()
        currentSpeedMbps <= 10.0 -> (0.10 + (currentSpeedMbps - 1.0) / 9.0 * 0.18).toFloat()
        currentSpeedMbps <= 50.0 -> (0.28 + (currentSpeedMbps - 10.0) / 40.0 * 0.18).toFloat()
        currentSpeedMbps <= 100.0 -> (0.46 + (currentSpeedMbps - 50.0) / 50.0 * 0.16).toFloat()
        currentSpeedMbps <= 500.0 -> (0.62 + (currentSpeedMbps - 100.0) / 400.0 * 0.20).toFloat()
        currentSpeedMbps <= 1000.0 -> (0.82 + (currentSpeedMbps - 500.0) / 500.0 * 0.14).toFloat()
        else -> (0.96 + (currentSpeedMbps - 1000.0) / 1500.0 * 0.04).toFloat().coerceAtMost(1f)
    }

    val animatedRatio by animateFloatAsState(
        targetValue = targetRatio,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "gaugeNeedle"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "hudGlowEffects")
    
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowIntensity"
    )

    val orbitalRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbitRotation"
    )

    val gaugeStartAngle = 135f
    val gaugeSweepAngle = 270f

    val activeColor = when (testStage) {
        TestStage.DOWNLOADING -> NeonCyan
        TestStage.UPLOADING -> ElectricBlue
        TestStage.BUFFERBLOAT -> GlowPink
        TestStage.PINGING, TestStage.CONNECTING_SERVER, TestStage.SWITCHING_SERVER -> NeonPurple
        TestStage.COMPLETED -> SuccessGreen
        else -> NeonCyan
    }

    Box(
        modifier = modifier
            .size(290.dp)
            .testTag("speed_gauge_container"),
        contentAlignment = Alignment.Center
    ) {
        // Deep radial glow spotlight behind the gauge
        Box(
            modifier = Modifier
                .size(260.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            activeColor.copy(alpha = if (isRunning) 0.15f * pulseGlow else 0.05f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Custom High-Precision Instrument Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val strokeWidth = 14.dp.toPx()
            val radius = (size.minDimension - strokeWidth * 2.8f) / 2f

            // 1. Outer Rotating Orbital Dashed Ring
            if (isRunning) {
                rotate(orbitalRotation, pivot = center) {
                    drawCircle(
                        color = NeonCyan.copy(alpha = 0.25f),
                        radius = radius + 22.dp.toPx(),
                        center = center,
                        style = Stroke(
                            width = 1.2.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 24f), 0f)
                        )
                    )
                }
            } else {
                drawCircle(
                    color = GaugeTrackBorder.copy(alpha = 0.6f),
                    radius = radius + 22.dp.toPx(),
                    center = center,
                    style = Stroke(
                        width = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 16f), 0f)
                    )
                )
            }

            // 2. Outer Static Thin Scale Guide Arc
            drawArc(
                color = GaugeTrackBorder,
                startAngle = gaugeStartAngle - 2f,
                sweepAngle = gaugeSweepAngle + 4f,
                useCenter = false,
                topLeft = Offset(center.x - (radius + 10.dp.toPx()), center.y - (radius + 10.dp.toPx())),
                size = Size((radius + 10.dp.toPx()) * 2, (radius + 10.dp.toPx()) * 2),
                style = Stroke(width = 1.dp.toPx(), cap = StrokeCap.Round)
            )

            // 3. Primary Recessed Gauge Groove Track
            drawArc(
                color = GaugeTrack,
                startAngle = gaugeStartAngle,
                sweepAngle = gaugeSweepAngle,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // 4. Active High-Tech Neon Sweep Arc
            val currentSweep = (gaugeSweepAngle * animatedRatio.coerceIn(0f, 1f)).coerceAtLeast(0f)
            if (currentSweep > 0.5f) {
                // Secondary outer neon glow stroke
                drawArc(
                    brush = Brush.sweepGradient(
                        0.0f to NeonPurple.copy(alpha = 0.3f),
                        0.4f to ElectricBlue.copy(alpha = 0.4f),
                        0.8f to NeonCyan.copy(alpha = 0.6f),
                        1.0f to NeonCyan.copy(alpha = 0.8f),
                        center = center
                    ),
                    startAngle = gaugeStartAngle,
                    sweepAngle = currentSweep,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth + 4.dp.toPx(), cap = StrokeCap.Round)
                )

                // Primary crisp laser arc
                drawArc(
                    brush = Brush.sweepGradient(
                        0.0f to NeonPurple,
                        0.35f to ElectricBlue,
                        0.75f to CyberBlue,
                        1.0f to NeonCyan,
                        center = center
                    ),
                    startAngle = gaugeStartAngle,
                    sweepAngle = currentSweep,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }

            // 5. Precision Tick Marks along the arc (Gigabit Scale)
            val majorTickRatios = listOf(0.0, 0.10, 0.28, 0.46, 0.62, 0.82, 1.0)
            val subTickRatios = listOf(0.05, 0.19, 0.37, 0.54, 0.72, 0.91)

            // Sub-ticks
            for (subRatio in subTickRatios) {
                val angleDeg = gaugeStartAngle + (gaugeSweepAngle * subRatio)
                val angleRad = Math.toRadians(angleDeg.toDouble())
                val innerR = radius - 14.dp.toPx()
                val outerR = radius - 8.dp.toPx()

                val startX = center.x + (innerR * cos(angleRad)).toFloat()
                val startY = center.y + (innerR * sin(angleRad)).toFloat()
                val endX = center.x + (outerR * cos(angleRad)).toFloat()
                val endY = center.y + (outerR * sin(angleRad)).toFloat()

                val isPassed = animatedRatio >= subRatio.toFloat()
                val tickColor = if (isPassed) activeColor.copy(alpha = 0.6f) else TextMuted.copy(alpha = 0.25f)

                drawLine(
                    color = tickColor,
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = 1.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            // Major ticks with illuminated notches
            for (i in majorTickRatios.indices) {
                val ratio = majorTickRatios[i]
                val angleDeg = gaugeStartAngle + (gaugeSweepAngle * ratio)
                val angleRad = Math.toRadians(angleDeg.toDouble())

                val innerR = radius - 18.dp.toPx()
                val outerR = radius - 6.dp.toPx()

                val startX = center.x + (innerR * cos(angleRad)).toFloat()
                val startY = center.y + (innerR * sin(angleRad)).toFloat()
                val endX = center.x + (outerR * cos(angleRad)).toFloat()
                val endY = center.y + (outerR * sin(angleRad)).toFloat()

                val isPassed = animatedRatio >= ratio.toFloat()
                val tickColor = if (isPassed) activeColor else TextMuted.copy(alpha = 0.45f)

                drawLine(
                    color = tickColor,
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = if (isPassed) 2.5.dp.toPx() else 1.8.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            // 6. Laser Head Bead & Needle Ray
            val needleAngleDeg = gaugeStartAngle + (gaugeSweepAngle * animatedRatio.coerceIn(0f, 1f))
            val needleAngleRad = Math.toRadians(needleAngleDeg.toDouble())

            val beadX = center.x + (radius * cos(needleAngleRad)).toFloat()
            val beadY = center.y + (radius * sin(needleAngleRad)).toFloat()

            if (animatedRatio > 0.01f) {
                drawCircle(
                    color = activeColor.copy(alpha = 0.4f * pulseGlow),
                    radius = 10.dp.toPx(),
                    center = Offset(beadX, beadY)
                )
                drawCircle(
                    color = Color.White,
                    radius = 3.5.dp.toPx(),
                    center = Offset(beadX, beadY)
                )
            }

            // Center Pin with metallic cyber ring
            drawCircle(
                color = GaugeTrackBorder,
                radius = 10.dp.toPx(),
                center = center
            )
            drawCircle(
                color = if (isRunning) activeColor else TextMuted,
                radius = 6.dp.toPx(),
                center = center
            )
            drawCircle(
                color = Color(0xFF0A101D),
                radius = 3.5.dp.toPx(),
                center = center
            )
        }

        // Center Digital HUD Display
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 65.dp)
        ) {
            val isGigabit = currentSpeedMbps >= 1000.0
            val speedDisplay = if (testStage == TestStage.IDLE) {
                "0.0"
            } else if (isGigabit) {
                String.format("%.2f", currentSpeedMbps / 1000.0)
            } else {
                String.format("%.1f", currentSpeedMbps)
            }

            // Digital Speed Readout
            Text(
                text = speedDisplay,
                fontSize = if (isGigabit) 44.sp else 48.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = TextPrimary,
                letterSpacing = (-1.5).sp,
                modifier = Modifier.testTag("current_speed_readout")
            )

            // Speed Unit Pill Badge
            Box(
                modifier = Modifier
                    .background(activeColor.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                    .border(1.dp, activeColor.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = if (isGigabit) "Gbps (Gigabit+)" else "Mbps",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = activeColor,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Stage Status Capsule with live pulsing indicator (English Only)
            val stageLabel = when (testStage) {
                TestStage.IDLE -> "Ready to Benchmark"
                TestStage.PREPARING, TestStage.CONNECTING_SERVER -> "Handshaking Edge Node..."
                TestStage.SWITCHING_SERVER -> "Switching Global Node..."
                TestStage.PINGING -> "Measuring Ping & Latency..."
                TestStage.DOWNLOADING -> "Testing Gigabit Download..."
                TestStage.UPLOADING -> "Testing Gigabit Upload..."
                TestStage.BUFFERBLOAT -> "Analyzing Bufferbloat Grade..."
                TestStage.COMPLETED -> "Gigabit Benchmark Verified"
                TestStage.ERROR -> "Connection Failed"
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color(0xFF0F172A).copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                    .border(1.dp, activeColor.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                if (isRunning) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(activeColor.copy(alpha = pulseGlow), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(
                    text = stageLabel,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (testStage == TestStage.ERROR) GlowPink else TextSecondary
                )
            }
        }
    }
}
