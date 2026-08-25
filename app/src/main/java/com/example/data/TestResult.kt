package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "speed_test_results")
data class TestResult(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val downloadSpeedMbps: Double,
    val uploadSpeedMbps: Double,
    val pingMs: Double,
    val jitterMs: Double,
    val packetLossPercent: Double,
    val loadedPingMs: Double,
    val bufferbloatGrade: String,
    val ispName: String,
    val publicIp: String,
    val serverName: String,
    val serverCountry: String,
    val networkType: String,
    val wifiSsid: String = "",
    val wifiRssiDbm: Int = 0,
    val isRealUnthrottled: Boolean = true
)
