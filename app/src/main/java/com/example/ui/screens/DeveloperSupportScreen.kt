package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.IspDetails
import com.example.model.WifiDetails
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberCardBorder
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
fun DeveloperSupportScreen(
    ispDetails: IspDetails,
    wifiDetails: WifiDetails,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(NeonCyan.copy(alpha = 0.15f), CircleShape)
                    .border(1.dp, NeonCyan.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SupportAgent,
                    contentDescription = "Support",
                    tint = NeonCyan,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Developer Support",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary
                )
                Text(
                    text = "Direct Assistance & Technical Inquiries",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Developer Profile Card
        DeveloperProfileCard()

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Direct Contact Buttons
        Text(
            text = "DIRECT CONTACT CHANNELS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            color = NeonCyan,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 1. Email Support
        SupportContactItem(
            title = "Gmail / Email Support",
            value = "alexraselchodhury@gmail.com",
            actionLabel = "Send Mail",
            badgeColor = Color(0xFFEA4335),
            icon = Icons.Default.Email,
            testTag = "support_channel_email",
            onOpen = { openUrl(context, "mailto:alexraselchodhury@gmail.com") },
            onCopy = { copyToClipboard(context, "alexraselchodhury@gmail.com", "Email copied to clipboard") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 2. WhatsApp Direct
        SupportContactItem(
            title = "WhatsApp Support",
            value = "+8801882278234",
            actionLabel = "Open Chat",
            badgeColor = Color(0xFF25D366),
            icon = Icons.Default.Chat,
            testTag = "support_channel_whatsapp",
            onOpen = { openUrl(context, "https://wa.me/8801882278234") },
            onCopy = { copyToClipboard(context, "+8801882278234", "WhatsApp number copied") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 3. Facebook Profile
        SupportContactItem(
            title = "Facebook Profile",
            value = "md.rasel.7.8.2.3.4",
            actionLabel = "View Profile",
            badgeColor = Color(0xFF1877F2),
            icon = Icons.Default.Public,
            testTag = "support_channel_facebook",
            onOpen = { openUrl(context, "https://www.facebook.com/md.rasel.7.8.2.3.4") },
            onCopy = { copyToClipboard(context, "https://www.facebook.com/md.rasel.7.8.2.3.4", "Facebook link copied") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 4. Telegram Channel
        SupportContactItem(
            title = "Telegram Official",
            value = "@HANTER_XD_OFFICIAL",
            actionLabel = "Open Telegram",
            badgeColor = Color(0xFF229ED9),
            icon = Icons.Default.Send,
            testTag = "support_channel_telegram",
            onOpen = { openUrl(context, "https://t.me/HANTER_XD_OFFICIAL") },
            onCopy = { copyToClipboard(context, "https://t.me/HANTER_XD_OFFICIAL", "Telegram link copied") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 5. Phone Call Hotline
        SupportContactItem(
            title = "Direct Hotline / Call",
            value = "+8801882278234",
            actionLabel = "Call Now",
            badgeColor = ElectricBlue,
            icon = Icons.Default.Call,
            testTag = "support_channel_phone",
            onOpen = { openUrl(context, "tel:+8801882278234") },
            onCopy = { copyToClipboard(context, "+8801882278234", "Hotline number copied") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Network Diagnostics Export Card for Fast Debugging
        DiagnosticsExportCard(
            ispDetails = ispDetails,
            wifiDetails = wifiDetails,
            onCopy = {
                val report = """
                    --- RealSpeed Network Diagnostic Report ---
                    Device: ${wifiDetails.userDeviceModel}
                    Network: ${if (wifiDetails.isConnected) "Wi-Fi (${wifiDetails.ssid})" else "Broadband"}
                    Router Brand: ${wifiDetails.routerBrand} (${wifiDetails.routerModel})
                    Router Gateway IP: ${wifiDetails.gatewayIp}
                    Wi-Fi Frequency: ${wifiDetails.frequencyBand} (Ch ${wifiDetails.channel})
                    Link Speed: ${wifiDetails.linkSpeedMbps} Mbps
                    ISP: ${ispDetails.ispName}
                    Public IP: ${ispDetails.publicIp}
                    ASN: ${ispDetails.asNumber}
                    Location: ${ispDetails.city}, ${ispDetails.country}
                    Engine: RealSpeed Anti-Fake Gigabit Core v2.5
                """.trimIndent()
                copyToClipboard(context, report, "Network diagnostic report copied to clipboard")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // App Architecture Card
        AppInfoCard()

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun DeveloperProfileCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(CyberSurface)
            .border(
                1.dp,
                Brush.horizontalGradient(
                    listOf(NeonCyan.copy(alpha = 0.6f), ElectricBlue.copy(alpha = 0.3f))
                ),
                RoundedCornerShape(18.dp)
            )
            .padding(16.dp)
            .testTag("developer_profile_card")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar Circle
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(
                            Brush.linearGradient(listOf(NeonCyan, ElectricBlue)),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "MR",
                        fontWeight = FontWeight.Black,
                        color = Color.Black,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "MD RASEL",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .background(SuccessGreen.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                .border(1.dp, SuccessGreen.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "VERIFIED DEVELOPER",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black,
                                color = SuccessGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Lead Android & Network Engine Engineer",
                        fontSize = 11.sp,
                        color = NeonCyan
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bio description
            Text(
                text = "Specializing in high-performance Gigabit network benchmarking, anti-throttling algorithms, real-time socket telemetry, and router hardware diagnostics.",
                fontSize = 11.sp,
                color = TextSecondary,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun SupportContactItem(
    title: String,
    value: String,
    actionLabel: String,
    badgeColor: Color,
    icon: ImageVector,
    testTag: String,
    onOpen: () -> Unit,
    onCopy: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(CyberSurface)
            .border(1.dp, CyberCardBorder, RoundedCornerShape(14.dp))
            .padding(12.dp)
            .testTag(testTag)
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
                        .size(40.dp)
                        .background(badgeColor.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                        .border(1.dp, badgeColor.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = badgeColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = value,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextSecondary
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Copy button
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(CyberSurfaceVariant, CircleShape)
                        .clickable { onCopy() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy",
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Action open button
                Box(
                    modifier = Modifier
                        .background(badgeColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                        .border(1.dp, badgeColor.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                        .clickable { onOpen() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = actionLabel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = badgeColor
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = "Open",
                            tint = badgeColor,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DiagnosticsExportCard(
    ispDetails: IspDetails,
    wifiDetails: WifiDetails,
    onCopy: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CyberSurfaceElevated)
            .border(1.dp, NeonPurple.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            .padding(14.dp)
            .testTag("diagnostics_export_card")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = "Report",
                        tint = NeonPurple,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Diagnostics Troubleshooting Report",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Need help with ISP speeds or router configuration? One-tap copy full system telemetry (Router, ISP, BSSID, Subnet) to share directly with the developer.",
                fontSize = 11.sp,
                color = TextSecondary,
                lineHeight = 15.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onCopy,
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonPurple.copy(alpha = 0.2f),
                    contentColor = NeonPurple
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NeonPurple.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    .testTag("copy_diagnostics_button")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy Report",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Copy Telemetry Report for Support",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun AppInfoCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(CyberSurfaceVariant)
            .border(1.dp, CyberCardBorder, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "App & Core Engine",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "v2.5.0 Pro",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = NeonCyan
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "RealSpeed Gigabit Engine with Anti-ISP Spoofing & Router OUI Hardware Intelligence. Built with Kotlin & Jetpack Compose.",
                fontSize = 10.sp,
                color = TextMuted,
                lineHeight = 14.sp
            )
        }
    }
}

private fun openUrl(context: Context, urlString: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Unable to launch link: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
    }
}

private fun copyToClipboard(context: Context, text: String, message: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    val clip = ClipData.newPlainText("RealSpeed Info", text)
    clipboard?.setPrimaryClip(clip)
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
