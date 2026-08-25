package com.example.ui.components

import android.graphics.Paint
import android.graphics.Typeface
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
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

/**
 * Ultra-Modern Futuristic Speedometer HUD Gauge.
 * Features:
 * - Dynamic calibrated cyber arc with multi-color neon laser sweep
 * - Distinctive Unique High-Energy "Uplink Transmission Beam" look during UPLOADING
 * - Stage-aware direction badge (⬇ DOWNLOAD / ⬆ UPLOAD UPLINK)
 * - Ultra-high precision digital speed counter
 * - Real-time animated telemetry rings and countdown capsule
 */
@Composable
fun SpeedGauge(
    currentSpeedMbps: Double,
    testStage: TestStage,
    progress: Float,
    remainingSeconds: Int = 0,
    isBengali: Boolean = false,
    modifier: Modifier = Modifier
) {
    val isRunning = testStage != TestStage.IDLE && testStage != TestStage.COMPLETED && testStage != TestStage.ERROR
    val isUploading = testStage == TestStage.UPLOADING

    // Calibrated logarithmic mapping for speed scale (0 to 1000+ Mbps Gigabit)
    val targetRatio = when {
        currentSpeedMbps <= 0.0 -> 0f
        currentSpeedMbps <= 1.0 -> (currentSpeedMbps / 1.0 * 0.08).toFloat()
        currentSpeedMbps <= 5.0 -> (0.08 + (currentSpeedMbps - 1.0) / 4.0 * 0.12).toFloat()
        currentSpeedMbps <= 25.0 -> (0.20 + (currentSpeedMbps - 5.0) / 20.0 * 0.18).toFloat()
        currentSpeedMbps <= 100.0 -> (0.38 + (currentSpeedMbps - 25.0) / 75.0 * 0.20).toFloat()
        currentSpeedMbps <= 250.0 -> (0.58 + (currentSpeedMbps - 100.0) / 150.0 * 0.16).toFloat()
        currentSpeedMbps <= 500.0 -> (0.74 + (currentSpeedMbps - 250.0) / 250.0 * 0.14).toFloat()
        currentSpeedMbps <= 1000.0 -> (0.88 + (currentSpeedMbps - 500.0) / 500.0 * 0.09).toFloat()
        else -> (0.97 + (currentSpeedMbps - 1000.0) / 2000.0 * 0.03).toFloat().coerceAtMost(1f)
    }

    val animatedRatio by animateFloatAsState(
        targetValue = targetRatio,
        animationSpec = tween(durationMillis = 260, easing = FastOutSlowInEasing),
        label = "gaugeNeedle"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "hudGlowEffects")

    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.40f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowIntensity"
    )

    // Upward beam pulse for upload
    val uploadBeamPulse by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "uploadBeamPulse"
    )

    val orbitalRotationCW by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (isUploading) 14000 else 24000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbitRotationCW"
    )

    val orbitalRotationCCW by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (isUploading) 10000 else 18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbitRotationCCW"
    )

    val gaugeStartAngle = 135f
    val gaugeSweepAngle = 270f

    // Distinctive color palettes: Magenta-Violet-Indigo for UPLOADING
    val activeColor = when (testStage) {
        TestStage.DOWNLOADING -> NeonCyan
        TestStage.UPLOADING -> Color(0xFFC084FC) // Vibrant Neon Amethyst / Magenta Purple
        TestStage.BUFFERBLOAT -> GlowPink
        TestStage.PINGING, TestStage.CONNECTING_SERVER, TestStage.SWITCHING_SERVER -> NeonPurple
        TestStage.COMPLETED -> SuccessGreen
        else -> NeonCyan
    }

    val activeGradient = when (testStage) {
        TestStage.UPLOADING -> listOf(
            Color(0xFF38BDF8), // Electric Sky Blue
            Color(0xFF818CF8), // Deep Indigo
            Color(0xFFC084FC), // Neon Amethyst
            Color(0xFFF472B6)  // Hyper Pink Beacon
        )
        TestStage.BUFFERBLOAT -> listOf(GlowPink, NeonPurple, ElectricBlue)
        TestStage.COMPLETED -> listOf(SuccessGreen, NeonCyan, ElectricBlue)
        else -> listOf(NeonPurple, ElectricBlue, NeonCyan)
    }

    Box(
        modifier = modifier
            .size(310.dp)
            .testTag("speed_gauge_container"),
        contentAlignment = Alignment.Center
    ) {
        // 1. Deep Ambient Radial Atmosphere Glow
        Box(
            modifier = Modifier
                .size(280.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            activeColor.copy(alpha = if (isRunning) (if (isUploading) 0.24f else 0.18f) * pulseGlow else 0.06f),
                            activeColor.copy(alpha = if (isRunning) 0.06f else 0.01f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // 2. Custom Futuristic Speedometer Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val strokeWidth = 13.dp.toPx()
            val radius = (size.minDimension - strokeWidth * 3.2f) / 2f

            // A. Dual Concentric Orbital Radar Dashed Rings
            if (isRunning) {
                // Clockwise Ring
                rotate(orbitalRotationCW, pivot = center) {
                    drawCircle(
                        color = activeColor.copy(alpha = if (isUploading) 0.32f else 0.20f),
                        radius = radius + 26.dp.toPx(),
                        center = center,
                        style = Stroke(
                            width = if (isUploading) 1.5.dp.toPx() else 1.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(
                                if (isUploading) floatArrayOf(12f, 16f) else floatArrayOf(8f, 20f),
                                0f
                            )
                        )
                    )
                }
                // Counter-Clockwise Inner Ring
                rotate(orbitalRotationCCW, pivot = center) {
                    drawCircle(
                        color = activeColor.copy(alpha = if (isUploading) 0.25f else 0.15f),
                        radius = radius + 20.dp.toPx(),
                        center = center,
                        style = Stroke(
                            width = 0.8.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(
                                if (isUploading) floatArrayOf(10f, 18f) else floatArrayOf(16f, 24f),
                                0f
                            )
                        )
                    )
                }
            } else {
                drawCircle(
                    color = GaugeTrackBorder.copy(alpha = 0.5f),
                    radius = radius + 22.dp.toPx(),
                    center = center,
                    style = Stroke(
                        width = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 14f), 0f)
                    )
                )
            }

            // B. Outer Metallic Border Guide Arc
            drawArc(
                color = GaugeTrackBorder.copy(alpha = 0.8f),
                startAngle = gaugeStartAngle - 2f,
                sweepAngle = gaugeSweepAngle + 4f,
                useCenter = false,
                topLeft = Offset(center.x - (radius + 8.dp.toPx()), center.y - (radius + 8.dp.toPx())),
                size = Size((radius + 8.dp.toPx()) * 2, (radius + 8.dp.toPx()) * 2),
                style = Stroke(width = 1.2.dp.toPx(), cap = StrokeCap.Round)
            )

            // C. Base Recessed Track Arc (Dark Tech Groove)
            drawArc(
                color = GaugeTrack,
                startAngle = gaugeStartAngle,
                sweepAngle = gaugeSweepAngle,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // D. Active Laser Sweep Arc with Multi-stop Gradient
            val currentSweep = (gaugeSweepAngle * animatedRatio.coerceIn(0f, 1f)).coerceAtLeast(0f)
            if (currentSweep > 0.5f) {
                // Outer Glow Blur Arc
                drawArc(
                    brush = Brush.sweepGradient(
                        0.0f to activeGradient[0].copy(alpha = 0.25f),
                        0.35f to activeGradient[1].copy(alpha = 0.45f),
                        0.70f to activeGradient[2].copy(alpha = 0.65f),
                        1.0f to activeGradient[activeGradient.size - 1].copy(alpha = 0.85f),
                        center = center
                    ),
                    startAngle = gaugeStartAngle,
                    sweepAngle = currentSweep,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth + 6.dp.toPx(), cap = StrokeCap.Round)
                )

                // Crisp Core Laser Stroke
                drawArc(
                    brush = Brush.sweepGradient(
                        0.0f to activeGradient[0],
                        0.35f to activeGradient[1],
                        0.70f to activeGradient[2],
                        1.0f to activeGradient[activeGradient.size - 1],
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

            // E. Precision Scale Markers & Scale Labels (0, 5, 25, 100, 250, 500, 1G)
            val scalePoints = listOf(
                Pair(0.00, "0"),
                Pair(0.08, "1"),
                Pair(0.20, "5"),
                Pair(0.38, "25"),
                Pair(0.58, "100"),
                Pair(0.74, "250"),
                Pair(0.88, "500"),
                Pair(0.97, "1G")
            )

            // Intermediate Sub-Ticks
            val subTickRatios = listOf(0.04, 0.14, 0.29, 0.48, 0.66, 0.81, 0.93)
            for (subRatio in subTickRatios) {
                val angleDeg = gaugeStartAngle + (gaugeSweepAngle * subRatio.toFloat())
                val angleRad = Math.toRadians(angleDeg.toDouble())
                val innerR = radius - 12.dp.toPx()
                val outerR = radius - 7.dp.toPx()

                val isPassed = animatedRatio >= subRatio.toFloat()
                val tickColor = if (isPassed) activeColor.copy(alpha = 0.55f) else TextMuted.copy(alpha = 0.2f)

                drawLine(
                    color = tickColor,
                    start = Offset(center.x + (innerR * cos(angleRad)).toFloat(), center.y + (innerR * sin(angleRad)).toFloat()),
                    end = Offset(center.x + (outerR * cos(angleRad)).toFloat(), center.y + (outerR * sin(angleRad)).toFloat()),
                    strokeWidth = 1.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            // Major Scale Ticks and Text
            val textPaint = Paint().apply {
                color = android.graphics.Color.parseColor("#64748B")
                textSize = 9.dp.toPx()
                typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
                textAlign = Paint.Align.CENTER
                isAntiAlias = true
            }

            val activeTextPaint = Paint().apply {
                color = activeColor.toArgb()
                textSize = 9.5.dp.toPx()
                typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
                textAlign = Paint.Align.CENTER
                isAntiAlias = true
            }

            for ((ratio, label) in scalePoints) {
                val angleDeg = gaugeStartAngle + (gaugeSweepAngle * ratio.toFloat())
                val angleRad = Math.toRadians(angleDeg.toDouble())

                val innerR = radius - 16.dp.toPx()
                val outerR = radius - 5.dp.toPx()
                val labelR = radius - 26.dp.toPx()

                val isPassed = animatedRatio >= ratio.toFloat()
                val tickColor = if (isPassed) activeColor else TextMuted.copy(alpha = 0.4f)

                drawLine(
                    color = tickColor,
                    start = Offset(center.x + (innerR * cos(angleRad)).toFloat(), center.y + (innerR * sin(angleRad)).toFloat()),
                    end = Offset(center.x + (outerR * cos(angleRad)).toFloat(), center.y + (outerR * sin(angleRad)).toFloat()),
                    strokeWidth = if (isPassed) 2.2.dp.toPx() else 1.5.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // Scale graduation text
                val labelX = center.x + (labelR * cos(angleRad)).toFloat()
                val labelY = center.y + (labelR * sin(angleRad)).toFloat() + 3.dp.toPx()

                drawIntoCanvas { canvas ->
                    canvas.nativeCanvas.drawText(
                        label,
                        labelX,
                        labelY,
                        if (isPassed) activeTextPaint else textPaint
                    )
                }
            }

            // F. Active Laser Head Bead with Luminous Particle Corona
            val needleAngleDeg = gaugeStartAngle + (gaugeSweepAngle * animatedRatio.coerceIn(0f, 1f))
            val needleAngleRad = Math.toRadians(needleAngleDeg.toDouble())
            val beadX = center.x + (radius * cos(needleAngleRad)).toFloat()
            val beadY = center.y + (radius * sin(needleAngleRad)).toFloat()

            if (animatedRatio > 0.005f) {
                // Wide soft corona
                drawCircle(
                    color = activeColor.copy(alpha = (if (isUploading) 0.45f else 0.35f) * pulseGlow),
                    radius = if (isUploading) 15.dp.toPx() else 12.dp.toPx(),
                    center = Offset(beadX, beadY)
                )
                // Mid halo
                drawCircle(
                    color = activeColor.copy(alpha = 0.90f),
                    radius = if (isUploading) 7.dp.toPx() else 6.dp.toPx(),
                    center = Offset(beadX, beadY)
                )
                // White laser nucleus
                drawCircle(
                    color = Color.White,
                    radius = 3.5.dp.toPx(),
                    center = Offset(beadX, beadY)
                )
            }

            // G. Special Unique Uploading Uplink Waves Animation
            if (isUploading) {
                val waveYOffset = uploadBeamPulse * 30.dp.toPx()
                val waveAlpha = (1f - uploadBeamPulse) * 0.7f
                drawLine(
                    color = activeColor.copy(alpha = waveAlpha),
                    start = Offset(center.x - 18.dp.toPx(), center.y - 45.dp.toPx() - waveYOffset),
                    end = Offset(center.x + 18.dp.toPx(), center.y - 45.dp.toPx() - waveYOffset),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            // H. Center Metallic Pivot Hub
            drawCircle(
                color = GaugeTrackBorder,
                radius = 8.dp.toPx(),
                center = center
            )
            drawCircle(
                color = if (isRunning) activeColor else TextMuted,
                radius = 5.dp.toPx(),
                center = center
            )
            drawCircle(
                color = Color(0xFF030712),
                radius = 3.dp.toPx(),
                center = center
            )
        }

        // 3. Ultra-Clean Digital HUD Telemetry Display
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 42.dp)
        ) {
            // Stage Indicator Pill Badge (e.g. ⬇ DOWNLOAD / ⬆ UPLOAD UPLINK / ⚡ PING)
            val stageMode = when (testStage) {
                TestStage.DOWNLOADING -> "DOWNLOAD"
                TestStage.UPLOADING -> "▲ UPLINK BEAM"
                TestStage.PINGING, TestStage.CONNECTING_SERVER, TestStage.SWITCHING_SERVER -> "LATENCY"
                TestStage.BUFFERBLOAT -> "QUALITY"
                TestStage.COMPLETED -> "VERIFIED"
                else -> "REALSPEED"
            }

            val stageIcon = when (testStage) {
                TestStage.DOWNLOADING -> Icons.Default.ArrowDownward
                TestStage.UPLOADING -> Icons.Default.ArrowUpward
                TestStage.COMPLETED -> Icons.Default.CheckCircle
                else -> Icons.Default.Speed
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(
                        color = activeColor.copy(alpha = if (isUploading) 0.18f else 0.12f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = activeColor.copy(alpha = if (isUploading) 0.55f else 0.35f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 3.dp)
            ) {
                Icon(
                    imageVector = stageIcon,
                    contentDescription = null,
                    tint = activeColor,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stageMode,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace,
                    color = activeColor,
                    letterSpacing = 1.5.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Main Digital Speed Number Readout
            val isGigabit = currentSpeedMbps >= 1000.0
            val speedDisplay = if (testStage == TestStage.IDLE) {
                "0.0"
            } else if (isGigabit) {
                String.format("%.2f", currentSpeedMbps / 1000.0)
            } else {
                String.format("%.1f", currentSpeedMbps)
            }

            Text(
                text = speedDisplay,
                fontSize = if (isGigabit) 44.sp else 50.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = if (isUploading) Color(0xFFF1F5F9) else TextPrimary,
                letterSpacing = (-2).sp,
                modifier = Modifier.testTag("current_speed_readout")
            )

            // Speed Unit Pill Badge
            Box(
                modifier = Modifier
                    .background(Color(0xFF0F172A), RoundedCornerShape(6.dp))
                    .border(0.8.dp, activeColor.copy(alpha = if (isUploading) 0.5f else 0.3f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = if (isGigabit) "Gbps" else "Mbps",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = activeColor,
                    letterSpacing = 0.8.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Real-Time Countdown & Status Message
            val stageLabel = when (testStage) {
                TestStage.IDLE -> "Ready to Benchmark"
                TestStage.PREPARING, TestStage.CONNECTING_SERVER -> "Handshaking Edge Node..."
                TestStage.SWITCHING_SERVER -> "Switching Global Node..."
                TestStage.PINGING -> "Measuring Ping & Jitter..."
                TestStage.DOWNLOADING -> if (remainingSeconds > 0) "Testing Download • ${remainingSeconds}s left" else "Testing Download..."
                TestStage.UPLOADING -> if (remainingSeconds > 0) "20s Sustained Uplink • ${remainingSeconds}s left" else "Testing Uplink..."
                TestStage.BUFFERBLOAT -> "Analyzing Quality & Jitter..."
                TestStage.COMPLETED -> "Bandwidth Benchmark Verified"
                TestStage.ERROR -> "Connection Failed"
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color(0xFF0A101D).copy(alpha = 0.90f), RoundedCornerShape(12.dp))
                    .border(1.dp, activeColor.copy(alpha = if (isUploading) 0.45f else 0.25f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                if (isRunning) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(activeColor.copy(alpha = pulseGlow), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(
                    text = stageLabel,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (testStage == TestStage.ERROR) GlowPink else (if (isUploading) Color(0xFFE2E8F0) else TextSecondary)
                )
            }
        }
    }
}
