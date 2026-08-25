package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.HopStatus
import com.example.model.IspDetails
import com.example.model.NetworkType
import com.example.model.ServerHopResult
import com.example.model.ServerLocation
import com.example.model.SpeedMetrics
import com.example.model.TestStage
import com.example.model.WifiDetails
import com.example.ui.components.LiveSpeedGraph
import com.example.ui.components.NetworkHeaderCard
import com.example.ui.components.SpeedGauge
import com.example.ui.components.SuitabilityBadges
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceElevated
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.DangerRed
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.GlowPink
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SpeedTestScreen(
    testStage: TestStage,
    speedMetrics: SpeedMetrics,
    networkType: NetworkType,
    ispDetails: IspDetails,
    wifiDetails: WifiDetails,
    selectedServer: ServerLocation,
    isBengali: Boolean = false,
    isMultiServerMode: Boolean,
    onToggleMultiServerMode: () -> Unit,
    onStartTest: () -> Unit,
    onCancelTest: () -> Unit,
    onSelectServerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isRunning = testStage != TestStage.IDLE && testStage != TestStage.COMPLETED && testStage != TestStage.ERROR
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Network & Authentic Provider Header Card
        NetworkHeaderCard(
            networkType = networkType,
            ispDetails = ispDetails,
            wifiDetails = wifiDetails,
            selectedServer = selectedServer,
            onSelectServerClick = onSelectServerClick,
            isBengali = false
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Multi-Country Global Verification Mode Switcher
        MultiCountryModeSelectorCard(
            isMultiServerMode = isMultiServerMode,
            isRunning = isRunning,
            onToggle = onToggleMultiServerMode
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Active Server Live Routing Status Badge
        if (isRunning || speedMetrics.serverHopResults.isNotEmpty()) {
            ActiveServerLiveBadge(
                speedMetrics = speedMetrics,
                selectedServer = selectedServer
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Live High-Precision Orbital Speedometer Gauge
        SpeedGauge(
            currentSpeedMbps = speedMetrics.currentMbps,
            testStage = testStage,
            progress = speedMetrics.progress,
            remainingSeconds = speedMetrics.remainingSeconds,
            isBengali = false
        )

        // Progress Bar for current stream / cycle
        val animatedProgress by animateFloatAsState(
            targetValue = if (isRunning) speedMetrics.progress else 0f,
            label = "testProgress"
        )
        val stageIndicatorColor = when (testStage) {
            TestStage.DOWNLOADING -> NeonCyan
            TestStage.UPLOADING -> Color(0xFFC084FC)
            TestStage.BUFFERBLOAT -> GlowPink
            TestStage.COMPLETED -> SuccessGreen
            else -> NeonCyan
        }
        if (isRunning) {
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth(0.72f)
                    .height(4.dp)
                    .clip(CircleShape),
                color = stageIndicatorColor,
                trackColor = CyberSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Real Telemetry Metrics Matrix (Download, Upload, Ping, Jitter)
        SpeedMetricsGrid(
            metrics = speedMetrics,
            testStage = testStage
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Multi-Country Edge Hop Routing Results
        if (isMultiServerMode && (isRunning || speedMetrics.serverHopResults.isNotEmpty())) {
            MultiCountryHopsCard(
                hopResults = speedMetrics.serverHopResults,
                currentHopIndex = speedMetrics.currentHopIndex,
                isRunning = isRunning
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Real-Time Oscillograph Bandwidth Telemetry Graph
        val peakSpeed = maxOf(speedMetrics.peakDownloadMbps, speedMetrics.peakUploadMbps, speedMetrics.currentMbps)
        LiveSpeedGraph(
            samples = speedMetrics.historySamples,
            currentSpeedMbps = speedMetrics.currentMbps,
            peakMbps = peakSpeed,
            testStage = testStage,
            isBengali = false
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Anti-ISP Spoof / Gigabit Fiber Defense Badge
        AntiFakeBypassCard()

        Spacer(modifier = Modifier.height(14.dp))

        // Suitability Badges (4K Stream, Gaming, Video Call, Bufferbloat)
        AnimatedVisibility(
            visible = testStage == TestStage.COMPLETED || (!isRunning && speedMetrics.downloadMbps > 0),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            SuitabilityBadges(
                downloadMbps = speedMetrics.downloadMbps,
                uploadMbps = speedMetrics.uploadMbps,
                pingMs = speedMetrics.pingMs,
                jitterMs = speedMetrics.jitterMs,
                bufferbloatGrade = speedMetrics.bufferbloatGrade,
                isBengali = false
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // High-Tech Action Button
        ProActionButton(
            isRunning = isRunning,
            testStage = testStage,
            isMultiServerMode = isMultiServerMode,
            onStartTest = onStartTest,
            onCancelTest = onCancelTest
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ProActionButton(
    isRunning: Boolean,
    testStage: TestStage,
    isMultiServerMode: Boolean,
    onStartTest: () -> Unit,
    onCancelTest: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "btnPulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "btnGlow"
    )

    if (isRunning) {
        Button(
            onClick = onCancelTest,
            colors = ButtonDefaults.buttonColors(
                containerColor = DangerRed.copy(alpha = 0.15f),
                contentColor = DangerRed
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .border(1.dp, DangerRed.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .testTag("stop_test_button")
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Stop,
                    contentDescription = "Stop",
                    tint = DangerRed,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Stop Benchmark",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF0077B6),
                            Color(0xFF00B4D8),
                            NeonCyan
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    1.dp,
                    NeonCyan.copy(alpha = pulseAlpha),
                    RoundedCornerShape(16.dp)
                )
                .clickable { onStartTest() }
                .testTag("start_test_button"),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (testStage == TestStage.COMPLETED) Icons.Default.Refresh else Icons.Default.PlayArrow,
                    contentDescription = "Start",
                    tint = Color(0xFF030712),
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = when {
                        testStage == TestStage.COMPLETED -> "Run Benchmark Again"
                        isMultiServerMode -> "Start Gigabit Multi-Node Test"
                        else -> "Start Gigabit Speed Test"
                    },
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF030712),
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

@Composable
private fun MultiCountryModeSelectorCard(
    isMultiServerMode: Boolean,
    isRunning: Boolean,
    onToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    listOf(CyberSurfaceElevated, CyberSurface)
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                1.dp,
                if (isMultiServerMode) NeonCyan.copy(alpha = 0.5f) else CyberCardBorder,
                RoundedCornerShape(16.dp)
            )
            .clickable(enabled = !isRunning) { onToggle() }
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .testTag("multi_country_mode_toggle_card")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(if (isMultiServerMode) NeonCyan.copy(alpha = 0.15f) else CyberBackground, RoundedCornerShape(10.dp))
                        .border(1.dp, if (isMultiServerMode) NeonCyan.copy(alpha = 0.4f) else CyberCardBorder, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = "Multi Country",
                        tint = if (isMultiServerMode) NeonCyan else TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Global Multi-Node Verification",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isMultiServerMode) NeonCyan else TextPrimary
                    )
                    Text(
                        text = "Benchmarks authentic international ISP & Gigabit speeds without local cache bias",
                        fontSize = 10.sp,
                        color = TextSecondary,
                        lineHeight = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Switch(
                checked = isMultiServerMode,
                onCheckedChange = { if (!isRunning) onToggle() },
                enabled = !isRunning,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF030712),
                    checkedTrackColor = NeonCyan,
                    uncheckedThumbColor = TextMuted,
                    uncheckedTrackColor = CyberBackground
                ),
                modifier = Modifier.testTag("multi_country_switch")
            )
        }
    }
}

@Composable
private fun ActiveServerLiveBadge(
    speedMetrics: SpeedMetrics,
    selectedServer: ServerLocation
) {
    val activeName = if (speedMetrics.activeServerName.isNotEmpty()) speedMetrics.activeServerName else selectedServer.name
    val activeFlag = if (speedMetrics.activeServerFlag.isNotEmpty()) speedMetrics.activeServerFlag else selectedServer.flagEmoji

    val infiniteTransition = rememberInfiniteTransition(label = "pulseBadge")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    listOf(
                        NeonCyan.copy(alpha = 0.12f),
                        NeonPurple.copy(alpha = 0.08f)
                    )
                ),
                RoundedCornerShape(12.dp)
            )
            .border(1.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .testTag("active_server_live_badge")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(SuccessGreen.copy(alpha = alphaAnim), CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$activeFlag $activeName",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            if (speedMetrics.totalHops > 1) {
                Box(
                    modifier = Modifier
                        .background(CyberBackground, RoundedCornerShape(6.dp))
                        .border(1.dp, CyberCardBorder, RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Node ${speedMetrics.currentHopIndex + 1}/${speedMetrics.totalHops}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = NeonCyan
                    )
                }
            }
        }
    }
}

@Composable
private fun MultiCountryHopsCard(
    hopResults: List<ServerHopResult>,
    currentHopIndex: Int,
    isRunning: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CyberSurfaceElevated, RoundedCornerShape(16.dp))
            .border(1.dp, CyberCardBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
            .testTag("multi_country_hops_card")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🌐",
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Multi-Country Verification Nodes",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                Text(
                    text = "Gigabit Edge",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    color = NeonCyan
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            hopResults.forEachIndexed { index, hop ->
                val isCurrentActive = isRunning && index == currentHopIndex
                val isCompleted = hop.status == HopStatus.COMPLETED

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .background(
                            if (isCurrentActive) NeonCyan.copy(alpha = 0.08f) else Color.Transparent,
                            RoundedCornerShape(8.dp)
                        )
                        .border(
                            if (isCurrentActive) 1.dp else 0.dp,
                            if (isCurrentActive) NeonCyan.copy(alpha = 0.3f) else Color.Transparent,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1.1f)
                    ) {
                        Text(
                            text = hop.server.flagEmoji,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = hop.server.name,
                                fontSize = 11.sp,
                                fontWeight = if (isCurrentActive) FontWeight.Black else FontWeight.SemiBold,
                                color = if (isCurrentActive) NeonCyan else TextPrimary
                            )
                            Text(
                                text = hop.server.regionName,
                                fontSize = 9.sp,
                                color = TextMuted
                            )
                        }
                    }

                    // Metrics column
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1.5f)
                    ) {
                        if (isCompleted) {
                            Column(horizontalAlignment = Alignment.End) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    val downText = if (hop.downloadMbps >= 1000.0) "↓${String.format("%.1f", hop.downloadMbps / 1000.0)}G" else "↓${String.format("%.1f", hop.downloadMbps)}M"
                                    val upText = if (hop.uploadMbps >= 1000.0) "↑${String.format("%.1f", hop.uploadMbps / 1000.0)}G" else "↑${String.format("%.1f", hop.uploadMbps)}M"

                                    Text(
                                        text = downText,
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        color = NeonCyan
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = upText,
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        color = ElectricBlue
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "${hop.pingMs.toInt()}ms",
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace,
                                        color = NeonPurple
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Completed",
                                tint = SuccessGreen,
                                modifier = Modifier.size(15.dp)
                            )
                        } else if (isCurrentActive) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = hop.stageDescription,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonCyan
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                CircularProgressIndicator(
                                    modifier = Modifier.size(12.dp),
                                    color = NeonCyan,
                                    strokeWidth = 1.5.dp
                                )
                            }
                        } else {
                            Text(
                                text = "Queued",
                                fontSize = 10.sp,
                                color = TextMuted
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AntiFakeBypassCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        SuccessGreen.copy(alpha = 0.08f),
                        CyberSurfaceElevated
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .border(1.dp, SuccessGreen.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            .padding(14.dp)
            .testTag("anti_fake_defense_card")
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(SuccessGreen.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                    .border(1.dp, SuccessGreen.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "Gigabit Certified",
                    tint = SuccessGreen,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "🛡️ 100% Authentic Bandwidth (Anti-ISP Spoof & Gigabit Ready)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = SuccessGreen
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "Local ISP cache mirrors can show artificial bursts. RealSpeed uses multi-socket unbuffered packets across global cloud edges to verify genuine ISP throughput for standard, Fiber & Gigabit connections.",
                    fontSize = 10.sp,
                    color = TextSecondary,
                    lineHeight = 14.sp
                )
            }
        }
    }
}

@Composable
private fun SpeedMetricsGrid(
    metrics: SpeedMetrics,
    testStage: TestStage
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("speed_metrics_grid"),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Download Card
        val (downVal, downUnit) = if (metrics.downloadMbps >= 1000.0) {
            Pair(String.format("%.2f", metrics.downloadMbps / 1000.0), "Gbps")
        } else {
            Pair(String.format("%.1f", metrics.downloadMbps), "Mbps")
        }

        MetricBox(
            title = "Download",
            value = downVal,
            unit = downUnit,
            icon = Icons.Default.ArrowDownward,
            iconColor = NeonCyan,
            isActive = testStage == TestStage.DOWNLOADING,
            modifier = Modifier.weight(1f)
        )

        // Upload Card
        val (upVal, upUnit) = if (metrics.uploadMbps >= 1000.0) {
            Pair(String.format("%.2f", metrics.uploadMbps / 1000.0), "Gbps")
        } else {
            Pair(String.format("%.1f", metrics.uploadMbps), "Mbps")
        }

        MetricBox(
            title = "Upload",
            value = upVal,
            unit = upUnit,
            icon = Icons.Default.ArrowUpward,
            iconColor = Color(0xFFC084FC),
            isActive = testStage == TestStage.UPLOADING,
            modifier = Modifier.weight(1f)
        )

        // Ping Card
        MetricBox(
            title = "Ping",
            value = if (metrics.pingMs > 0) "${metrics.pingMs.toInt()}" else "--",
            unit = "ms",
            icon = Icons.Default.Timer,
            iconColor = NeonPurple,
            isActive = testStage == TestStage.PINGING,
            modifier = Modifier.weight(1f)
        )

        // Jitter Card
        MetricBox(
            title = "Jitter",
            value = if (metrics.jitterMs > 0) String.format("%.1f", metrics.jitterMs) else "--",
            unit = "ms",
            icon = Icons.Default.Waves,
            iconColor = GlowPink,
            isActive = false,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun MetricBox(
    title: String,
    value: String,
    unit: String,
    icon: ImageVector,
    iconColor: Color,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    val bgColor = if (isActive) iconColor.copy(alpha = 0.08f) else CyberSurfaceElevated

    Box(
        modifier = modifier
            .background(bgColor, RoundedCornerShape(14.dp))
            .border(
                1.dp,
                if (isActive) iconColor.copy(alpha = 0.7f) else CyberCardBorder,
                RoundedCornerShape(14.dp)
            )
            .padding(vertical = 10.dp, horizontal = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = title,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = TextPrimary
            )
            Text(
                text = unit,
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = iconColor.copy(alpha = 0.8f)
            )
        }
    }
}
