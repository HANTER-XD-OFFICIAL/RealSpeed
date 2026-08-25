package com.example.ui.screens

import android.content.Intent
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TestResult
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.DangerRed
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    results: List<TestResult>,
    averageDownload: Double?,
    maxDownload: Double?,
    onDeleteItem: (Long) -> Unit,
    onClearAll: () -> Unit,
    onOpenSupport: () -> Unit = {},
    isBengali: Boolean = false,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground)
            .padding(16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Speed Benchmark History",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary
                )
                Text(
                    text = "${results.size} authentic speed logs saved locally",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Developer Support Quick Button
                Button(
                    onClick = onOpenSupport,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyberSurfaceVariant,
                        contentColor = NeonCyan
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("history_support_button")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.SupportAgent,
                            contentDescription = "Support",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Support",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (results.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(
                        onClick = onClearAll,
                        modifier = Modifier.testTag("clear_history_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Clear All",
                            tint = DangerRed
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Summary Stats Row (Avg Download, Peak Record, Total Tests)
        if (results.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val avgDown = averageDownload ?: 0.0
                val (avgVal, avgUnit) = if (avgDown >= 1000.0) Pair(String.format("%.2f", avgDown / 1000.0), "Gbps") else Pair(String.format("%.1f", avgDown), "Mbps")
                SummaryStatBox(
                    label = "Avg Download",
                    value = avgVal,
                    unit = avgUnit,
                    color = NeonCyan,
                    modifier = Modifier.weight(1f)
                )

                val maxDown = maxDownload ?: 0.0
                val (maxVal, maxUnit) = if (maxDown >= 1000.0) Pair(String.format("%.2f", maxDown / 1000.0), "Gbps") else Pair(String.format("%.1f", maxDown), "Mbps")
                SummaryStatBox(
                    label = "Peak Record",
                    value = maxVal,
                    unit = maxUnit,
                    color = SuccessGreen,
                    modifier = Modifier.weight(1f)
                )

                SummaryStatBox(
                    label = "Total Tests",
                    value = "${results.size}",
                    unit = "runs",
                    color = ElectricBlue,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        if (results.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "Empty History",
                        tint = TextMuted,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No Speed Tests Yet",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    )
                    Text(
                        text = "Run a test to save authentic speed logs here",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(results, key = { it.id }) { item ->
                    HistoryItemCard(
                        result = item,
                        onDelete = { onDeleteItem(item.id) },
                        onShare = {
                            val downStr = if (item.downloadSpeedMbps >= 1000.0) "${String.format("%.2f", item.downloadSpeedMbps / 1000.0)} Gbps" else "${item.downloadSpeedMbps} Mbps"
                            val upStr = if (item.uploadSpeedMbps >= 1000.0) "${String.format("%.2f", item.uploadSpeedMbps / 1000.0)} Gbps" else "${item.uploadSpeedMbps} Mbps"
                            val shareText = "⚡ RealSpeed Benchmark Result:\n" +
                                    "📥 Download: $downStr\n" +
                                    "📤 Upload: $upStr\n" +
                                    "⏱️ Ping: ${item.pingMs} ms (Jitter: ${item.jitterMs} ms)\n" +
                                    "🛡️ ISP: ${item.ispName}\n" +
                                    "🌐 Server: ${item.serverName}\n" +
                                    "📊 Bufferbloat: Grade ${item.bufferbloatGrade}\n" +
                                    "Verified via RealSpeed Anti-Fake Gigabit Engine"
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, shareText)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share Test Result"))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SummaryStatBox(
    label: String,
    value: String,
    unit: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(CyberSurface, RoundedCornerShape(14.dp))
            .border(1.dp, CyberCardBorder.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
            .padding(vertical = 10.dp, horizontal = 8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(text = label, fontSize = 10.sp, color = TextSecondary, maxLines = 1)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Black,
                color = color
            )
            Text(text = unit, fontSize = 9.sp, color = TextMuted)
        }
    }
}

@Composable
private fun HistoryItemCard(
    result: TestResult,
    onDelete: () -> Unit,
    onShare: () -> Unit
) {
    val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(result.timestamp))
    val isWifi = result.networkType == "WIFI"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CyberSurface, RoundedCornerShape(16.dp))
            .border(1.dp, CyberCardBorder.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
            .padding(14.dp)
            .testTag("history_item_${result.id}")
    ) {
        Column {
            // Top: Date, Network Type & Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isWifi) Icons.Default.Wifi else Icons.Default.CellTower,
                        contentDescription = "Network",
                        tint = if (isWifi) NeonCyan else ElectricBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isWifi && result.wifiSsid.isNotEmpty()) result.wifiSsid else result.ispName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = dateStr,
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(onClick = onShare, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = TextSecondary, modifier = Modifier.size(16.dp))
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "Delete", tint = DangerRed.copy(alpha = 0.8f), modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Metrics row (Down, Up, Ping, Bufferbloat)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Download
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ArrowDownward, contentDescription = "Down", tint = NeonCyan, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = "Down", fontSize = 10.sp, color = TextSecondary)
                    }
                    val downDisplay = if (result.downloadSpeedMbps >= 1000.0) "${String.format("%.2f", result.downloadSpeedMbps / 1000.0)} Gbps" else "${result.downloadSpeedMbps} Mbps"
                    Text(
                        text = downDisplay,
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                // Upload
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ArrowUpward, contentDescription = "Up", tint = ElectricBlue, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = "Up", fontSize = 10.sp, color = TextSecondary)
                    }
                    val upDisplay = if (result.uploadSpeedMbps >= 1000.0) "${String.format("%.2f", result.uploadSpeedMbps / 1000.0)} Gbps" else "${result.uploadSpeedMbps} Mbps"
                    Text(
                        text = upDisplay,
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                // Ping
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Timer, contentDescription = "Ping", tint = NeonPurple, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = "Ping", fontSize = 10.sp, color = TextSecondary)
                    }
                    Text(
                        text = "${result.pingMs.toInt()} ms",
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                // Bufferbloat Grade Badge
                Box(
                    modifier = Modifier
                        .background(NeonCyan.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Grade ${result.bufferbloatGrade}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = NeonCyan
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${result.serverName} • ${result.publicIp}",
                fontSize = 10.sp,
                color = TextMuted
            )
        }
    }
}
