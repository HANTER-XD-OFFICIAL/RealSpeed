package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.Lan
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.VpnLock
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.IspDetails
import com.example.model.NetworkType
import com.example.model.ServerLocation
import com.example.model.WifiDetails
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCardBorderGlow
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceElevated
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber

@Composable
fun NetworkHeaderCard(
    networkType: NetworkType,
    ispDetails: IspDetails,
    wifiDetails: WifiDetails,
    selectedServer: ServerLocation,
    onSelectServerClick: () -> Unit,
    isBengali: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        CyberSurfaceElevated,
                        CyberSurface
                    )
                ),
                shape = RoundedCornerShape(18.dp)
            )
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    listOf(
                        CyberCardBorderGlow.copy(alpha = 0.8f),
                        CyberCardBorder.copy(alpha = 0.5f)
                    )
                ),
                shape = RoundedCornerShape(18.dp)
            )
            .padding(14.dp)
            .testTag("network_header_card")
    ) {
        Column {
            // Row 1: Wi-Fi / Connection Name, Router Hardware & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val (netIcon, netColor) = when (networkType) {
                    NetworkType.WIFI -> Pair(Icons.Default.Wifi, NeonCyan)
                    NetworkType.CELLULAR_5G -> Pair(Icons.Default.CellTower, ElectricBlue)
                    NetworkType.CELLULAR_4G -> Pair(Icons.Default.CellTower, ElectricBlue)
                    NetworkType.ETHERNET -> Pair(Icons.Default.Lan, SuccessGreen)
                    NetworkType.VPN -> Pair(Icons.Default.VpnLock, WarningAmber)
                    else -> Pair(Icons.Default.Public, TextSecondary)
                }

                // Glowing Network Type Icon Avatar
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(netColor.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                        .border(1.dp, netColor.copy(alpha = 0.35f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = netIcon,
                        contentDescription = "Network type",
                        tint = netColor,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (networkType == NetworkType.WIFI && wifiDetails.isConnected) {
                                wifiDetails.ssid
                            } else {
                                ispDetails.ispName.ifEmpty { "Broadband Network" }
                            },
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    // Router Brand / Wi-Fi Specs
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Router,
                            contentDescription = "Router",
                            tint = NeonCyan,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (networkType == NetworkType.WIFI && wifiDetails.isConnected) {
                                "${wifiDetails.routerBrand} • ${wifiDetails.frequencyBand}"
                            } else {
                                "${ispDetails.ispName} • ${ispDetails.connectionMedium}"
                            },
                            fontSize = 11.sp,
                            color = TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Gigabit & Real Speed Verified Telemetry Pill
                Box(
                    modifier = Modifier
                        .background(SuccessGreen.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                        .border(1.dp, SuccessGreen.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Gigabit Edge",
                            tint = SuccessGreen,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Gigabit Line",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Row 2: ISP Provider & User Device Info Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CyberBackground.copy(alpha = 0.7f), RoundedCornerShape(10.dp))
                    .border(1.dp, CyberCardBorder.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // ISP Info
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1.1f)) {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = "ISP",
                        tint = ElectricBlue,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "ISP: ${ispDetails.ispName.ifEmpty { "Broadband" }}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                // User Device Info
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(0.9f)) {
                    Icon(
                        imageVector = Icons.Default.Smartphone,
                        contentDescription = "Device",
                        tint = NeonPurple,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = wifiDetails.userDeviceModel.split("(").firstOrNull()?.trim() ?: "Android",
                        fontSize = 10.sp,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Row 3: Public IP Pill & Edge Target Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Public IP Pill
                Box(
                    modifier = Modifier
                        .background(CyberSurfaceVariant, RoundedCornerShape(10.dp))
                        .border(1.dp, CyberCardBorder, RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(NeonCyan, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "IP: ${ispDetails.publicIp.ifEmpty { "127.0.0.1" }}",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }

                // Server Selector Quick-Switch Pill
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(CyberSurfaceVariant, CyberSurfaceElevated)
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .border(1.dp, NeonCyan.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                        .clickable { onSelectServerClick() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("server_select_pill")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = selectedServer.flagEmoji,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = selectedServer.name,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                maxLines = 1
                            )
                            val pingDisplay = selectedServer.latencyMs?.let { "${it.toInt()}ms latency" } ?: "Auto Edge Node"
                            Text(
                                text = pingDisplay,
                                fontSize = 9.sp,
                                color = NeonCyan
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = "Change Server",
                            tint = TextSecondary,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        }
    }
}
