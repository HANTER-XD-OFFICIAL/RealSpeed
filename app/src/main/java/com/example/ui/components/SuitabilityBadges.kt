package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BufferbloatGrade
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.DangerRed
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber

@Composable
fun SuitabilityBadges(
    downloadMbps: Double,
    uploadMbps: Double,
    pingMs: Double,
    jitterMs: Double,
    bufferbloatGrade: BufferbloatGrade,
    isBengali: Boolean = false,
    modifier: Modifier = Modifier
) {
    val gamingQuality = when {
        pingMs <= 35 && jitterMs <= 6 -> QualityLevel.EXCELLENT
        pingMs <= 70 && jitterMs <= 15 -> QualityLevel.GOOD
        pingMs <= 120 -> QualityLevel.FAIR
        else -> QualityLevel.POOR
    }

    val streaming4kQuality = when {
        downloadMbps >= 30.0 -> QualityLevel.EXCELLENT
        downloadMbps >= 15.0 -> QualityLevel.GOOD
        downloadMbps >= 5.0 -> QualityLevel.FAIR
        else -> QualityLevel.POOR
    }

    val videoCallQuality = when {
        downloadMbps >= 5.0 && uploadMbps >= 3.0 && pingMs <= 80 -> QualityLevel.EXCELLENT
        downloadMbps >= 2.0 && uploadMbps >= 1.0 -> QualityLevel.GOOD
        else -> QualityLevel.FAIR
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("suitability_badges_container")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Real Network Experience Rating",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary
            )
            Text(
                text = "Gigabit Certified",
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                color = NeonCyan
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SuitabilityItem(
                title = "Gaming",
                icon = Icons.Default.Gamepad,
                level = gamingQuality,
                subText = "${pingMs.toInt()}ms latency",
                modifier = Modifier.weight(1f)
            )

            SuitabilityItem(
                title = "4K Stream",
                icon = Icons.Default.LiveTv,
                level = streaming4kQuality,
                subText = if (downloadMbps >= 1000.0) "${String.format("%.1f", downloadMbps / 1000.0)} Gbps" else "${downloadMbps.toInt()} Mbps",
                modifier = Modifier.weight(1f)
            )

            SuitabilityItem(
                title = "Video Call",
                icon = Icons.Default.Videocam,
                level = videoCallQuality,
                subText = if (uploadMbps > 0) "${uploadMbps.toInt()}M Up" else "HD",
                modifier = Modifier.weight(1f)
            )

            // Bufferbloat Item
            BufferbloatItem(
                grade = bufferbloatGrade,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

enum class QualityLevel(val labelEn: String, val color: Color) {
    EXCELLENT("A+ Top", SuccessGreen),
    GOOD("Good", ElectricBlue),
    FAIR("Fair", WarningAmber),
    POOR("Poor", DangerRed)
}

@Composable
private fun SuitabilityItem(
    title: String,
    icon: ImageVector,
    level: QualityLevel,
    subText: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(CyberSurface, RoundedCornerShape(12.dp))
            .border(1.dp, CyberCardBorder, RoundedCornerShape(12.dp))
            .padding(vertical = 10.dp, horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = level.color,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = level.labelEn,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                color = level.color
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subText,
                fontSize = 9.sp,
                color = TextMuted,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun BufferbloatItem(
    grade: BufferbloatGrade,
    modifier: Modifier = Modifier
) {
    val gradeColor = Color(grade.colorHex)

    Box(
        modifier = modifier
            .background(CyberSurface, RoundedCornerShape(12.dp))
            .border(1.dp, gradeColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(vertical = 10.dp, horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Speed,
                contentDescription = "Bufferbloat",
                tint = gradeColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Bloat",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = grade.grade,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = gradeColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Lag Grade",
                fontSize = 9.sp,
                color = TextMuted
            )
        }
    }
}
