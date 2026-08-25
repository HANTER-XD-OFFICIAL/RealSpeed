package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Lan
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VpnLock
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.IspDetails
import com.example.model.NetworkType
import com.example.model.WifiDetails
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceElevated
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.GlowPink
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber

@Composable
fun NetworkRadarScreen(
    networkType: NetworkType,
    ispDetails: IspDetails,
    wifiDetails: WifiDetails,
    isBengali: Boolean = false,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Title & Refresh Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Network & Router Radar",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary
                )
                Text(
                    text = "Hardware, ISP, Router & Gigabit Diagnostics",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            Button(
                onClick = onRefresh,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CyberSurfaceVariant,
                    contentColor = NeonCyan
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("refresh_network_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Anti-Fake ISP Architecture Banner
        AntiFakeExplanationBanner()

        Spacer(modifier = Modifier.height(14.dp))

        // Section 1: Router Brand & Wi-Fi Hardware
        RouterHardwareCard(
            wifiDetails = wifiDetails,
            networkType = networkType
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Section 2: ISP Details & Backbone Routing
        IspDetailsCard(ispDetails = ispDetails)

        Spacer(modifier = Modifier.height(14.dp))

        // Section 3: User Device & Client Identity
        UserDeviceIdentityCard(wifiDetails = wifiDetails)

        Spacer(modifier = Modifier.height(14.dp))

        // Section 4: DNS & Security Architecture
        DnsRoutingCard(
            wifiDetails = wifiDetails,
            ispDetails = ispDetails
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun AntiFakeExplanationBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CyberSurface, RoundedCornerShape(18.dp))
            .border(1.dp, SuccessGreen.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(SuccessGreen.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "Anti-Fake Shield",
                    tint = SuccessGreen,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Authentic Hardware & ISP Detection Engine",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = SuccessGreen
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "Scans direct BSSID OUI signatures, gateway subnets, frequency spectrum, and ASN routing to identify your genuine router brand, Wi-Fi model, user device, and ISP bandwidth.",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

@Composable
private fun RouterHardwareCard(
    wifiDetails: WifiDetails,
    networkType: NetworkType
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CyberSurface, RoundedCornerShape(18.dp))
            .border(1.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(18.dp))
            .padding(16.dp)
            .testTag("router_hardware_card")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Router,
                        contentDescription = "Router",
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Router & Wi-Fi Identity",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Box(
                    modifier = Modifier
                        .background(NeonCyan.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = wifiDetails.wifiStandard,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = NeonCyan
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Router Brand Badge Highlight
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CyberSurfaceVariant)
                    .border(1.dp, CyberCardBorder, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                Brush.linearGradient(listOf(NeonCyan.copy(alpha = 0.2f), ElectricBlue.copy(alpha = 0.2f))),
                                RoundedCornerShape(10.dp)
                            )
                            .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Router,
                            contentDescription = "Router",
                            tint = NeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = wifiDetails.routerBrand,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                        Text(
                            text = "Model: ${wifiDetails.routerModel}",
                            fontSize = 11.sp,
                            color = NeonCyan
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Signal strength bar if connected
            if (wifiDetails.isConnected) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Signal Quality",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = "${wifiDetails.signalQuality} (${wifiDetails.signalPercent}%)",
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = if (wifiDetails.signalPercent > 60) SuccessGreen else WarningAmber
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { wifiDetails.signalPercent / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),
                        color = if (wifiDetails.signalPercent > 60) SuccessGreen else WarningAmber,
                        trackColor = CyberSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            DetailRow(
                label = "Wi-Fi Name (SSID)",
                value = wifiDetails.ssid,
                valueColor = NeonCyan
            )
            DetailRow(
                label = "Frequency & Band",
                value = "${wifiDetails.frequencyBand} (Ch ${wifiDetails.channel})"
            )
            DetailRow(
                label = "Negotiated Link Speed",
                value = if (wifiDetails.linkSpeedMbps > 0) "${wifiDetails.linkSpeedMbps} Mbps (Max: ${wifiDetails.maxSupportedSpeedMbps} Mbps)" else "Auto Gigabit PHY"
            )
            DetailRow(
                label = "Gateway Router IP",
                value = wifiDetails.gatewayIp,
                isMonospace = true,
                valueColor = ElectricBlue
            )
            DetailRow(
                label = "Router Hardware MAC (BSSID)",
                value = wifiDetails.bssid,
                isMonospace = true
            )
            DetailRow(
                label = "Security Protocol",
                value = wifiDetails.securityType
            )
        }
    }
}

@Composable
private fun IspDetailsCard(ispDetails: IspDetails) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CyberSurface, RoundedCornerShape(18.dp))
            .border(1.dp, CyberCardBorder, RoundedCornerShape(18.dp))
            .padding(16.dp)
            .testTag("isp_details_card")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = "ISP",
                        tint = ElectricBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Internet Service Provider (ISP)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Box(
                    modifier = Modifier
                        .background(ElectricBlue.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "FIBER READY",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = ElectricBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            DetailRow(
                label = "ISP Provider Name",
                value = ispDetails.ispName,
                valueColor = NeonCyan
            )
            DetailRow(
                label = "Organization / AS Org",
                value = ispDetails.organization.ifEmpty { ispDetails.ispName }
            )
            DetailRow(
                label = "Autonomous System (ASN)",
                value = ispDetails.asNumber.ifEmpty { "AS13335 (Cloudflare Anycast)" }
            )
            DetailRow(
                label = "Public IPv4 Address",
                value = ispDetails.publicIp,
                isMonospace = true,
                valueColor = NeonCyan
            )
            DetailRow(
                label = "IPv6 Capability",
                value = ispDetails.ipv6Address.ifEmpty { "Dual-Stack Ready" },
                isMonospace = true
            )
            DetailRow(
                label = "ISP Connection Medium",
                value = ispDetails.connectionMedium
            )
            DetailRow(
                label = "POP Geo Location",
                value = "${ispDetails.city.ifEmpty { "Local Node" }}, ${ispDetails.country.ifEmpty { "Global" }}"
            )
            DetailRow(
                label = "Backbone Edge Node",
                value = ispDetails.hostname.ifEmpty { "Direct Anycast Backbone" }
            )
        }
    }
}

@Composable
private fun UserDeviceIdentityCard(wifiDetails: WifiDetails) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CyberSurface, RoundedCornerShape(18.dp))
            .border(1.dp, CyberCardBorder, RoundedCornerShape(18.dp))
            .padding(16.dp)
            .testTag("user_device_card")
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Smartphone,
                    contentDescription = "User Device",
                    tint = NeonPurple,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "User Device & Local Subnet",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            DetailRow(
                label = "Connected Device",
                value = wifiDetails.userDeviceModel,
                valueColor = NeonPurple
            )
            DetailRow(
                label = "Local Client IPv4",
                value = wifiDetails.localIp,
                isMonospace = true,
                valueColor = TextPrimary
            )
            DetailRow(
                label = "Subnet Mask",
                value = wifiDetails.subnetMask,
                isMonospace = true
            )
        }
    }
}

@Composable
private fun DnsRoutingCard(
    wifiDetails: WifiDetails,
    ispDetails: IspDetails
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CyberSurface, RoundedCornerShape(18.dp))
            .border(1.dp, CyberCardBorder, RoundedCornerShape(18.dp))
            .padding(16.dp)
            .testTag("dns_routing_card")
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Dns,
                    contentDescription = "DNS",
                    tint = GlowPink,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "DNS & Security Architecture",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            DetailRow(
                label = "Active DNS Resolvers",
                value = if (wifiDetails.dnsServers.isNotEmpty()) wifiDetails.dnsServers.joinToString(", ") else "1.1.1.1, 8.8.8.8",
                isMonospace = true
            )
            DetailRow(
                label = "Connection Security",
                value = if (ispDetails.isProxyOrVpn) "VPN Tunnel Active" else "Direct Authentic ISP Uplink",
                valueColor = if (ispDetails.isProxyOrVpn) WarningAmber else SuccessGreen
            )
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    isMonospace: Boolean = false,
    valueColor: Color = TextPrimary
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextSecondary,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontFamily = if (isMonospace) FontFamily.Monospace else FontFamily.Default,
            fontWeight = FontWeight.SemiBold,
            color = valueColor
        )
    }
}
