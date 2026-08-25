package com.example.model

enum class TestStage {
    IDLE,
    PREPARING,
    CONNECTING_SERVER,
    PINGING,
    DOWNLOADING,
    UPLOADING,
    BUFFERBLOAT,
    SWITCHING_SERVER,
    COMPLETED,
    ERROR
}

enum class NetworkType {
    WIFI,
    CELLULAR_5G,
    CELLULAR_4G,
    CELLULAR_3G,
    ETHERNET,
    VPN,
    OFFLINE,
    UNKNOWN
}

enum class HopStatus {
    PENDING,
    ACTIVE,
    COMPLETED,
    FAILED
}

data class ServerHopResult(
    val server: ServerLocation,
    val pingMs: Double = 0.0,
    val jitterMs: Double = 0.0,
    val downloadMbps: Double = 0.0,
    val uploadMbps: Double = 0.0,
    val status: HopStatus = HopStatus.PENDING,
    val stageDescription: String = "Pending"
)

data class SpeedSample(
    val timestampMs: Long,
    val speedMbps: Double,
    val isDownload: Boolean
)

data class IspDetails(
    val publicIp: String = "Fetching...",
    val ipv6Address: String = "",
    val ispName: String = "Detecting ISP...",
    val organization: String = "",
    val asNumber: String = "",
    val city: String = "",
    val region: String = "",
    val country: String = "",
    val countryCode: String = "",
    val timezone: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isCloudflareEdge: Boolean = false,
    val hostname: String = "",
    val isProxyOrVpn: Boolean = false,
    val connectionMedium: String = "Fiber Optic / GPON Broadband",
    val ispTier: String = "Gigabit Fiber Line"
)

data class WifiDetails(
    val isConnected: Boolean = false,
    val ssid: String = "Unknown WiFi",
    val bssid: String = "--:--:--:--",
    val routerBrand: String = "Universal Router",
    val routerModel: String = "Gigabit Dual-Band AC/AX",
    val userDeviceModel: String = "Android Device",
    val rssiDbm: Int = -100,
    val signalPercent: Int = 0,
    val signalQuality: String = "Good",
    val frequencyMhz: Int = 0,
    val frequencyBand: String = "5 GHz",
    val channel: Int = 36,
    val wifiStandard: String = "Wi-Fi 5 / 6",
    val linkSpeedMbps: Int = 0,
    val maxSupportedSpeedMbps: Int = 1200,
    val securityType: String = "WPA2/WPA3-Personal",
    val localIp: String = "0.0.0.0",
    val gatewayIp: String = "0.0.0.0",
    val subnetMask: String = "255.255.255.0 (/24)",
    val dnsServers: List<String> = emptyList(),
    val is5GHz: Boolean = false,
    val is6GHz: Boolean = false
)

data class ServerLocation(
    val id: String,
    val name: String,
    val regionName: String,
    val country: String,
    val countryCode: String,
    val flagEmoji: String,
    val provider: String,
    val downloadBaseUrl: String,
    val uploadUrl: String,
    val pingUrl: String,
    var latencyMs: Double? = null,
    val isEdgeAnycast: Boolean = false
)

enum class BufferbloatGrade(val grade: String, val descriptionEn: String, val descriptionBn: String, val colorHex: Long) {
    A_PLUS("A+", "Excellent - Minimal bufferbloat (<5ms delta)", "চমৎকার - কোন ল্যাগ নেই (<৫ms বৃদ্ধি)", 0xFF00F5D4),
    A("A", "Great - Ideal for fast competitive gaming (<15ms)", "অসাধারণ - গেমিং ও লাইভের জন্য উপযুক্ত (<১৫ms)", 0xFF10B981),
    B("B", "Good - Minor latency increase under load (<40ms)", "ভালো - লোডের সময় সামান্য লেটেন্সি (<৪০ms)", 0xFF3B82F6),
    C("C", "Fair - Noticeable buffer delay under load (<100ms)", "মোটামুটি - লোডের সময় বাফারব্লোট (<১০০ms)", 0xFFF59E0B),
    D("D", "Poor - High latency spikes during downloads (>100ms)", "দুর্বল - ডাউনলোড চলাকালে পিং অনেক বেড়ে যায়", 0xFFEF4444),
    F("F", "Critical - Severe ISP queueing delay (>250ms)", "অত্যন্ত খারাপ - তীব্র বাফার জ্যাম", 0xFFDC2626)
}

data class SpeedMetrics(
    val downloadMbps: Double = 0.0,
    val uploadMbps: Double = 0.0,
    val currentMbps: Double = 0.0,
    val peakDownloadMbps: Double = 0.0,
    val peakUploadMbps: Double = 0.0,
    val pingMs: Double = 0.0,
    val minPingMs: Double = 0.0,
    val maxPingMs: Double = 0.0,
    val jitterMs: Double = 0.0,
    val packetLossPercent: Double = 0.0,
    val loadedPingDownloadMs: Double = 0.0,
    val loadedPingUploadMs: Double = 0.0,
    val bufferbloatGrade: BufferbloatGrade = BufferbloatGrade.A,
    val totalBytesDownloaded: Long = 0L,
    val totalBytesUploaded: Long = 0L,
    val currentChunkInfo: String = "",
    val progress: Float = 0f,
    val remainingSeconds: Int = 0,
    val elapsedSeconds: Int = 0,
    val historySamples: List<SpeedSample> = emptyList(),
    // Multi-Country Server verification fields
    val isMultiServerMode: Boolean = true,
    val currentHopIndex: Int = 0,
    val totalHops: Int = 1,
    val activeServerName: String = "",
    val activeServerFlag: String = "",
    val activeServerCountry: String = "",
    val serverHopResults: List<ServerHopResult> = emptyList(),
    val antiFakeBypassVerified: Boolean = true,
    val statusMessageBn: String = "",
    val statusMessageEn: String = ""
)
