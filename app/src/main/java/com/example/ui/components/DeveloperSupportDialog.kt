package com.example.ui.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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

@Composable
fun DeveloperSupportDialog(
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        listOf(NeonCyan.copy(alpha = 0.6f), ElectricBlue.copy(alpha = 0.2f))
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .testTag("developer_support_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CyberSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(NeonCyan.copy(alpha = 0.15f), CircleShape)
                                .border(1.dp, NeonCyan.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SupportAgent,
                                contentDescription = "Support Icon",
                                tint = NeonCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Developer Support",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                                color = TextPrimary
                            )
                            Text(
                                text = "Direct Inquiry & Assistance",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.size(32.dp).testTag("close_support_dialog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Developer Verified Profile Badge
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(CyberSurfaceVariant)
                        .border(1.dp, CyberCardBorder, RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
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
                                fontSize = 16.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "MD RASEL",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .background(SuccessGreen.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 5.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "DEVELOPER",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SuccessGreen
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Gigabit Core Engine & System Architecture",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Privacy Notice Indicator
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(NeonPurple.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Secure",
                        tint = NeonPurple,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "One-tap direct access with end-to-end privacy",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = NeonPurple
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 1. Gmail / Email Support Button
                SupportActionButton(
                    title = "Email Support",
                    subtitle = "Official Inquiry & Feedback",
                    badgeColor = Color(0xFFEA4335), // Google Red
                    iconVector = Icons.Default.Email,
                    tag = "btn_contact_email",
                    onClick = {
                        openUrlSafely(context, "mailto:alexraselchodhury@gmail.com")
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 2. WhatsApp Support Button
                SupportActionButton(
                    title = "WhatsApp Chat",
                    subtitle = "Fast Messaging & Assistance",
                    badgeColor = Color(0xFF25D366), // WhatsApp Emerald Green
                    iconVector = Icons.Default.SupportAgent,
                    tag = "btn_contact_whatsapp",
                    onClick = {
                        openUrlSafely(context, "https://wa.me/8801882278234")
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 3. Facebook Profile Button
                SupportActionButton(
                    title = "Facebook Profile",
                    subtitle = "Social Connect & Updates",
                    badgeColor = Color(0xFF1877F2), // Facebook Blue
                    iconVector = Icons.Default.OpenInNew,
                    tag = "btn_contact_facebook",
                    onClick = {
                        openUrlSafely(context, "https://www.facebook.com/md.rasel.7.8.2.3.4")
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 4. Telegram Chat Button
                SupportActionButton(
                    title = "Telegram Channel",
                    subtitle = "Community & Support Channel",
                    badgeColor = Color(0xFF229ED9), // Telegram Cyan Blue
                    iconVector = Icons.Default.Send,
                    tag = "btn_contact_telegram",
                    onClick = {
                        openUrlSafely(context, "https://t.me/HANTER_XD_OFFICIAL")
                    }
                )
            }
        }
    }
}

@Composable
private fun SupportActionButton(
    title: String,
    subtitle: String,
    badgeColor: Color,
    iconVector: ImageVector,
    tag: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CyberSurfaceVariant)
            .border(1.dp, CyberCardBorder, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp)
            .testTag(tag)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(badgeColor.copy(alpha = 0.18f), RoundedCornerShape(10.dp))
                        .border(1.dp, badgeColor.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = iconVector,
                        contentDescription = title,
                        tint = badgeColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = subtitle,
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.OpenInNew,
                contentDescription = "Open",
                tint = TextMuted,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

fun openUrlSafely(context: Context, urlString: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Unable to launch link: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
    }
}
