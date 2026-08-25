package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.VerifiedUser
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
                    .size(42.dp)
                    .background(NeonCyan.copy(alpha = 0.15f), CircleShape)
                    .border(1.dp, NeonCyan.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SupportAgent,
                    contentDescription = "Support",
                    tint = NeonCyan,
                    modifier = Modifier.size(24.dp)
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
                    text = "Instant 1-Tap Secure Assistance",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Developer Profile Card (Privacy Protected)
        DeveloperProfileCard()

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Direct Contact Buttons Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Security,
                contentDescription = null,
                tint = NeonCyan,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "1-TAP DIRECT CHANNELS (TAP TO OPEN)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = NeonCyan,
                letterSpacing = 0.5.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 1. Email Support (Protected Masked Link)
        ProtectedContactCard(
            title = "Gmail / Email Support",
            subtitle = "Protected Support Mailbox • Tap to Launch",
            actionLabel = "Send Mail",
            badgeColor = Color(0xFFEA4335),
            icon = Icons.Default.Email,
            testTag = "support_channel_email",
            onClick = { openUrl(context, "mailto:alexraselchodhury@gmail.com") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 2. WhatsApp Direct (Protected Masked Link)
        ProtectedContactCard(
            title = "WhatsApp Official Support",
            subtitle = "Direct Message Support • Tap to Chat",
            actionLabel = "Open WhatsApp",
            badgeColor = Color(0xFF25D366),
            icon = Icons.Default.Chat,
            testTag = "support_channel_whatsapp",
            onClick = { openUrl(context, "https://wa.me/8801882278234") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 3. Facebook Profile (Protected Masked Link)
        ProtectedContactCard(
            title = "Facebook Official Support",
            subtitle = "Direct Messenger • Tap to Connect",
            actionLabel = "View Profile",
            badgeColor = Color(0xFF1877F2),
            icon = Icons.Default.Public,
            testTag = "support_channel_facebook",
            onClick = { openUrl(context, "https://www.facebook.com/md.rasel.7.8.2.3.4") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 4. Telegram Channel (Protected Masked Link)
        ProtectedContactCard(
            title = "Telegram Official Support",
            subtitle = "Official Channel • Tap to Open",
            actionLabel = "Open Telegram",
            badgeColor = Color(0xFF229ED9),
            icon = Icons.Default.Send,
            testTag = "support_channel_telegram",
            onClick = { openUrl(context, "https://t.me/HANTER_XD_OFFICIAL") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 5. Phone Call Hotline (Protected Masked Link)
        ProtectedContactCard(
            title = "Direct Voice Hotline",
            subtitle = "Dedicated Assistance • Tap to Dial",
            actionLabel = "Call Now",
            badgeColor = ElectricBlue,
            icon = Icons.Default.Call,
            testTag = "support_channel_phone",
            onClick = { openUrl(context, "tel:+8801882278234") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Privacy Guarantee Card
        PrivacyBadgeCard()

        Spacer(modifier = Modifier.height(14.dp))

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
                        .size(50.dp)
                        .background(
                            Brush.linearGradient(listOf(NeonCyan, ElectricBlue)),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = "Verified Developer",
                        tint = Color.Black,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "MD RASEL",
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .background(SuccessGreen.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                .border(1.dp, SuccessGreen.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "DEVELOPER",
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
                text = "Direct technical support for Gigabit network benchmarking, anti-throttling inquiries, and router hardware diagnostics. Tap any channel below to connect instantly.",
                fontSize = 11.sp,
                color = TextSecondary,
                lineHeight = 16.sp
            )
        }
    }
}

/**
 * Clean & Secure Protected Contact Card.
 * No raw phone numbers, email addresses, or account handles are visible in plain text.
 * Users can simply tap the card or action button to launch the corresponding app directly.
 */
@Composable
private fun ProtectedContactCard(
    title: String,
    subtitle: String,
    actionLabel: String,
    badgeColor: Color,
    icon: ImageVector,
    testTag: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(CyberSurface)
            .border(1.dp, CyberCardBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(14.dp)
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
                        .size(42.dp)
                        .background(badgeColor.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                        .border(1.dp, badgeColor.copy(alpha = 0.45f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = badgeColor,
                        modifier = Modifier.size(22.dp)
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
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }

            // Clean 1-Tap Action Button
            Box(
                modifier = Modifier
                    .background(badgeColor.copy(alpha = 0.18f), RoundedCornerShape(10.dp))
                    .border(1.dp, badgeColor.copy(alpha = 0.55f), RoundedCornerShape(10.dp))
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

@Composable
private fun PrivacyBadgeCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(CyberSurfaceElevated)
            .border(1.dp, SuccessGreen.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(SuccessGreen.copy(alpha = 0.15f), CircleShape)
                    .border(1.dp, SuccessGreen.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = SuccessGreen,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "🔒 Direct 1-Tap Secure Channel",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SuccessGreen
                )
                Text(
                    text = "All support links launch securely into your phone's official applications without exposing cleartext credentials.",
                    fontSize = 10.sp,
                    color = TextSecondary,
                    lineHeight = 14.sp
                )
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
                text = "RealSpeed Sustained 20s High-Precision Gigabit Engine with Anti-ISP Spoofing & Router Hardware Intelligence. Built with Kotlin & Jetpack Compose.",
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
