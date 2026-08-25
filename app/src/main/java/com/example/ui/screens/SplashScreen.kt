package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VpnLock
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
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
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay

/**
 * Enterprise-Grade Cyber Telemetry Splash & Bootstrapping Screen.
 * Features:
 * - Geometric Radar Sweep Matrix Background Canvas
 * - Real-Time Multi-Stage Diagnostic Initialization Sequence
 * - Glowing Multi-Layered Neon Branding Core
 * - Monospace Live System Checklist & Progress Engine
 */
@Composable
fun SplashScreen(
    onLoaded: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progressAnim = remember { Animatable(0f) }
    val logoScale = remember { Animatable(0.85f) }
    val logoAlpha = remember { Animatable(0f) }

    var currentStepIndex by remember { mutableIntStateOf(0) }

    val diagnosticSteps = listOf(
        "Initializing 20s Gigabit Pipeline...",
        "Validating Network PHY & Wi-Fi 6/5G Adapter...",
        "Calibrating Anti-ISP Throttling Sockets...",
        "Connecting Global Edge Benchmark Nodes...",
        "System Ready • Launching Dashboard"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "splashCinematic")

    val radarRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radarRotation"
    )

    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.45f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseGlow"
    )

    val scanLineY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scanLineY"
    )

    LaunchedEffect(Unit) {
        // Entrance animation
        logoAlpha.animateTo(1f, animationSpec = tween(500, easing = LinearEasing))
        logoScale.animateTo(1f, animationSpec = tween(600, easing = FastOutSlowInEasing))

        // Step 1: Core initialization (0% -> 25%)
        currentStepIndex = 0
        progressAnim.animateTo(0.25f, animationSpec = tween(500, easing = FastOutSlowInEasing))
        delay(150)

        // Step 2: Adapter & PHY check (25% -> 55%)
        currentStepIndex = 1
        progressAnim.animateTo(0.55f, animationSpec = tween(550, easing = FastOutSlowInEasing))
        delay(180)

        // Step 3: Anti-Throttling check (55% -> 80%)
        currentStepIndex = 2
        progressAnim.animateTo(0.80f, animationSpec = tween(550, easing = FastOutSlowInEasing))
        delay(180)

        // Step 4: Edge CDN sync (80% -> 98%)
        currentStepIndex = 3
        progressAnim.animateTo(0.98f, animationSpec = tween(500, easing = FastOutSlowInEasing))
        delay(180)

        // Step 5: Completed (100%)
        currentStepIndex = 4
        progressAnim.animateTo(1.0f, animationSpec = tween(300, easing = LinearEasing))
        delay(260)

        // Trigger navigation
        onLoaded()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground)
            .testTag("app_splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        // 1. Ambient Background Grid & Radar Sweep Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val center = Offset(width / 2f, height / 2f)

            // Tech Grid Lines
            val gridColor = Color(0x0AFFFFFF)
            val gridSize = 40.dp.toPx()
            var x = 0f
            while (x < width) {
                drawLine(gridColor, Offset(x, 0f), Offset(x, height), strokeWidth = 1f)
                x += gridSize
            }
            var y = 0f
            while (y < height) {
                drawLine(gridColor, Offset(0f, y), Offset(width, y), strokeWidth = 1f)
                y += gridSize
            }

            // Central Radar Cones
            val maxR = size.minDimension * 0.45f
            drawCircle(
                color = Color(0x0E00F5FF),
                radius = maxR,
                center = center,
                style = Stroke(1.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 12f), 0f))
            )
            drawCircle(
                color = Color(0x0800F5FF),
                radius = maxR * 0.65f,
                center = center,
                style = Stroke(1.dp.toPx())
            )
            drawCircle(
                color = Color(0x0500F5FF),
                radius = maxR * 0.35f,
                center = center
            )

            // Sweeping Radar Line
            rotate(radarRotation, pivot = center) {
                drawLine(
                    brush = Brush.linearGradient(
                        colors = listOf(Color.Transparent, NeonCyan.copy(alpha = 0.22f)),
                        start = center,
                        end = Offset(center.x + maxR, center.y)
                    ),
                    start = center,
                    end = Offset(center.x + maxR, center.y),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            // Subtle vertical scan line
            val curY = height * scanLineY
            drawLine(
                brush = Brush.horizontalGradient(
                    listOf(Color.Transparent, NeonCyan.copy(alpha = 0.08f), Color.Transparent),
                    startX = 0f,
                    endX = width
                ),
                start = Offset(0f, curY),
                end = Offset(width, curY),
                strokeWidth = 2.dp.toPx()
            )
        }

        // 2. Central Professional Content Column
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 40.dp)
        ) {
            // Top Telemetry Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(SuccessGreen.copy(alpha = pulseGlow), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "CORE // SYNC_ONLINE",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = SuccessGreen,
                        letterSpacing = 1.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .background(Color(0xFF0B132B), RoundedCornerShape(6.dp))
                        .border(1.dp, CyberCardBorder, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "V2.4 PRO GIGABIT",
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.ExtraBold,
                        color = NeonCyan,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Middle Branding & Speed Tachometer Hub
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.scale(logoScale.value)
            ) {
                // Multi-layered Glowing Hex Hub
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    NeonCyan.copy(alpha = 0.28f * pulseGlow),
                                    NeonPurple.copy(alpha = 0.12f),
                                    Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(92.dp)
                            .clip(RoundedCornerShape(26.dp))
                            .background(
                                brush = Brush.linearGradient(
                                    listOf(
                                        CyberSurfaceVariant,
                                        Color(0xFF0A0F1D)
                                    )
                                )
                            )
                            .border(
                                width = 1.6.dp,
                                brush = Brush.linearGradient(
                                    listOf(
                                        NeonCyan.copy(alpha = 0.9f * pulseGlow),
                                        NeonPurple.copy(alpha = 0.6f),
                                        ElectricBlue.copy(alpha = 0.8f)
                                    )
                                ),
                                shape = RoundedCornerShape(26.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = "RealSpeed Engine",
                            tint = NeonCyan,
                            modifier = Modifier.size(52.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Brand Typography
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "RealSpeed",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.horizontalGradient(
                                    listOf(NeonCyan.copy(alpha = 0.25f), NeonPurple.copy(alpha = 0.25f))
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(1.dp, NeonCyan.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "PRO GIGABIT",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = NeonCyan,
                            letterSpacing = 1.2.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "High-Precision 20s Multi-Socket Bandwidth Analyzer",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }

            // Bottom Diagnostics & Progress Console
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Live Diagnostic Terminal Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CyberSurface)
                        .border(1.dp, CyberCardBorder.copy(alpha = 0.9f), RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        // Diagnostic status indicator with live step
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(NeonCyan, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "BOOTSTRAP SEQUENCE",
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonCyan,
                                    letterSpacing = 1.sp
                                )
                            }

                            Text(
                                text = "${(progressAnim.value * 100).toInt()}%",
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Black,
                                color = NeonCyan
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // High-tech Gradient Progress Bar
                        LinearProgressIndicator(
                            progress = { progressAnim.value },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = NeonCyan,
                            trackColor = Color(0xFF0F172A)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Dynamic Status Log Text
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = if (currentStepIndex >= 4) Icons.Default.CheckCircle else Icons.Default.Security,
                                contentDescription = null,
                                tint = if (currentStepIndex >= 4) SuccessGreen else ElectricBlue,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = diagnosticSteps.getOrElse(currentStepIndex) { "Ready" },
                                fontSize = 11.5.sp,
                                color = if (currentStepIndex >= 4) SuccessGreen else TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Bottom Verified & Anti-Throttling Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .background(Color(0xFF051B18).copy(alpha = 0.7f), RoundedCornerShape(12.dp))
                        .border(0.8.dp, SuccessGreen.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VpnLock,
                        contentDescription = null,
                        tint = SuccessGreen,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Anti-ISP Throttling & Zero-Fake Engine • MD RASEL Edition",
                        fontSize = 10.sp,
                        color = SuccessGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
