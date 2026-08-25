package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ServerLocation
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber

@Composable
fun ServersScreen(
    serversList: List<ServerLocation>,
    selectedServer: ServerLocation,
    onSelectServer: (ServerLocation) -> Unit,
    onPingAll: () -> Unit,
    isBengali: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground)
            .padding(16.dp)
    ) {
        // Title & Ping All action
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Global Edge Servers",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary
                )
                Text(
                    text = "Test latency & Gigabit throughput to international edge nodes",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            Button(
                onClick = onPingAll,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CyberSurfaceVariant,
                    contentColor = NeonCyan
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("ping_all_servers_button")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Ping All",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Ping All",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Info Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CyberSurface, RoundedCornerShape(14.dp))
                .border(1.dp, CyberCardBorder.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "💡", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Select any international location to benchmark overseas bandwidth and routing paths bypassing local ISP throttling.",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(serversList, key = { it.id }) { server ->
                val isSelected = server.id == selectedServer.id
                val cardBorder = if (isSelected) NeonCyan else CyberCardBorder.copy(alpha = 0.6f)
                val cardBg = if (isSelected) CyberSurfaceVariant else CyberSurface

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(cardBg, RoundedCornerShape(16.dp))
                        .border(if (isSelected) 1.5.dp else 1.dp, cardBorder, RoundedCornerShape(16.dp))
                        .clickable { onSelectServer(server) }
                        .padding(14.dp)
                        .testTag("server_item_${server.id}")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Country Flag Emoji / Edge Icon
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(CyberSurface, CircleShape)
                                .border(1.dp, CyberCardBorder, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = server.flagEmoji,
                                fontSize = 20.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Name & Region
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = server.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                if (server.isEdgeAnycast) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .background(NeonCyan.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "Recommended",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = NeonCyan
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "${server.regionName} • ${server.provider}",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }

                        // Latency badge
                        val latency = server.latencyMs
                        if (latency != null) {
                            val latencyColor = when {
                                latency <= 45.0 -> SuccessGreen
                                latency <= 100.0 -> ElectricBlue
                                latency <= 200.0 -> WarningAmber
                                else -> TextMuted
                            }

                            Box(
                                modifier = Modifier
                                    .background(latencyColor.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                                    .border(1.dp, latencyColor.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${latency.toInt()} ms",
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = latencyColor
                                )
                            }
                        }

                        if (isSelected) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Selected",
                                tint = NeonCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
