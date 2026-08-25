package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.TestResult
import com.example.data.TestResultRepository
import com.example.model.BufferbloatGrade
import com.example.model.IspDetails
import com.example.model.NetworkType
import com.example.model.ServerLocation
import com.example.model.SpeedMetrics
import com.example.model.TestStage
import com.example.model.WifiDetails
import com.example.network.NetworkScanner
import com.example.network.ServerEndpoints
import com.example.network.SpeedEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.max

class SpeedTestViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TestResultRepository
    private val networkScanner: NetworkScanner
    private val speedEngine: SpeedEngine

    init {
        val db = AppDatabase.getDatabase(application)
        repository = TestResultRepository(db.testResultDao())
        networkScanner = NetworkScanner(application)
        speedEngine = SpeedEngine()
    }

    val historyResults: StateFlow<List<TestResult>> = repository.allResults
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val averageDownload: StateFlow<Double?> = repository.averageDownload
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    val maxDownload: StateFlow<Double?> = repository.maxDownload
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    private val _testStage = MutableStateFlow(TestStage.IDLE)
    val testStage: StateFlow<TestStage> = _testStage.asStateFlow()

    private val _speedMetrics = MutableStateFlow(SpeedMetrics())
    val speedMetrics: StateFlow<SpeedMetrics> = _speedMetrics.asStateFlow()

    private val _networkType = MutableStateFlow(NetworkType.UNKNOWN)
    val networkType: StateFlow<NetworkType> = _networkType.asStateFlow()

    private val _ispDetails = MutableStateFlow(IspDetails())
    val ispDetails: StateFlow<IspDetails> = _ispDetails.asStateFlow()

    private val _wifiDetails = MutableStateFlow(WifiDetails())
    val wifiDetails: StateFlow<WifiDetails> = _wifiDetails.asStateFlow()

    private val _selectedServer = MutableStateFlow(ServerEndpoints.CLOUDFLARE_ANYCAST)
    val selectedServer: StateFlow<ServerLocation> = _selectedServer.asStateFlow()

    private val _serversList = MutableStateFlow(ServerEndpoints.SERVERS_LIST)
    val serversList: StateFlow<List<ServerLocation>> = _serversList.asStateFlow()

    private val _isBengali = MutableStateFlow(false)
    val isBengali: StateFlow<Boolean> = _isBengali.asStateFlow()

    private val _isMultiServerMode = MutableStateFlow(false) // Standard Sustained Single Server Mode default
    val isMultiServerMode: StateFlow<Boolean> = _isMultiServerMode.asStateFlow()

    private var activeTestJob: Job? = null

    init {
        refreshNetworkDetails()
        pingAllServers()
    }

    fun toggleLanguage() {
        _isBengali.value = !_isBengali.value
    }

    fun setLanguage(bengali: Boolean) {
        _isBengali.value = bengali
    }

    fun toggleMultiServerMode() {
        _isMultiServerMode.value = !_isMultiServerMode.value
    }

    fun setMultiServerMode(enabled: Boolean) {
        _isMultiServerMode.value = enabled
    }

    fun refreshNetworkDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            _networkType.value = networkScanner.detectNetworkType()
            _wifiDetails.value = networkScanner.getLocalWifiDetails()
            val isp = networkScanner.fetchPublicIspDetails()
            _ispDetails.value = isp
        }
    }

    fun selectServer(server: ServerLocation) {
        _selectedServer.value = server
    }

    fun pingAllServers() {
        viewModelScope.launch(Dispatchers.IO) {
            val updated = _serversList.value.map { srv ->
                val ping = speedEngine.measurePingOnly(srv)
                srv.copy(latencyMs = ping.avgPing)
            }
            _serversList.value = updated

            // If using Auto Anycast, update its latency
            val currentSelected = _selectedServer.value
            val match = updated.find { it.id == currentSelected.id }
            if (match != null) {
                _selectedServer.value = match
            }
        }
    }

    fun startSpeedTest() {
        if (_testStage.value != TestStage.IDLE && _testStage.value != TestStage.COMPLETED && _testStage.value != TestStage.ERROR) {
            return
        }

        activeTestJob?.cancel()
        _testStage.value = TestStage.PREPARING
        _speedMetrics.value = SpeedMetrics(isMultiServerMode = _isMultiServerMode.value)

        activeTestJob = viewModelScope.launch(Dispatchers.IO) {
            refreshNetworkDetails()
            delay(300)

            try {
                if (_isMultiServerMode.value) {
                    // Pick 4 diverse international transit edge locations to eliminate ISP caching
                    val multiServers = listOfNotNull(
                        _serversList.value.find { it.id == "sg_singapore" } ?: _serversList.value.getOrNull(1),
                        _serversList.value.find { it.id == "in_mumbai" } ?: _serversList.value.getOrNull(2),
                        _serversList.value.find { it.id == "de_frankfurt" } ?: _serversList.value.getOrNull(4),
                        _serversList.value.find { it.id == "us_ashburn" } ?: _serversList.value.getOrNull(6)
                    ).distinctBy { it.id }

                    val finalMetrics = speedEngine.runMultiCountrySpeedTest(
                        servers = multiServers,
                        onUpdate = { live ->
                            _speedMetrics.value = live
                        },
                        onStageChange = { stage, bnMsg, enMsg ->
                            _testStage.value = stage
                        }
                    )

                    _testStage.value = TestStage.COMPLETED
                    _speedMetrics.value = finalMetrics

                    // Save to Room Database
                    val resultEntity = TestResult(
                        downloadSpeedMbps = finalMetrics.downloadMbps,
                        uploadSpeedMbps = finalMetrics.uploadMbps,
                        pingMs = finalMetrics.pingMs,
                        jitterMs = finalMetrics.jitterMs,
                        packetLossPercent = finalMetrics.packetLossPercent,
                        loadedPingMs = max(finalMetrics.loadedPingDownloadMs, finalMetrics.loadedPingUploadMs),
                        bufferbloatGrade = finalMetrics.bufferbloatGrade.grade,
                        ispName = _ispDetails.value.ispName,
                        publicIp = _ispDetails.value.publicIp,
                        serverName = "Global Multi-Country Route (${multiServers.size} Nodes)",
                        serverCountry = "🇸🇬 🇮🇳 🇩🇪 🇺🇸 International",
                        networkType = _networkType.value.name,
                        wifiSsid = if (_networkType.value == NetworkType.WIFI) _wifiDetails.value.ssid else "",
                        wifiRssiDbm = if (_networkType.value == NetworkType.WIFI) _wifiDetails.value.rssiDbm else 0,
                        isRealUnthrottled = true
                    )
                    repository.insertResult(resultEntity)
                } else {
                    val finalMetrics = speedEngine.runFullSpeedTest(
                        server = _selectedServer.value,
                        onUpdate = { live ->
                            _speedMetrics.value = live
                        },
                        onStageChange = { stageName ->
                            _testStage.value = when (stageName) {
                                "PINGING" -> TestStage.PINGING
                                "DOWNLOADING" -> TestStage.DOWNLOADING
                                "UPLOADING" -> TestStage.UPLOADING
                                "BUFFERBLOAT" -> TestStage.BUFFERBLOAT
                                else -> TestStage.PREPARING
                            }
                        }
                    )

                    _testStage.value = TestStage.COMPLETED
                    _speedMetrics.value = finalMetrics

                    // Save to Room Database
                    val resultEntity = TestResult(
                        downloadSpeedMbps = finalMetrics.downloadMbps,
                        uploadSpeedMbps = finalMetrics.uploadMbps,
                        pingMs = finalMetrics.pingMs,
                        jitterMs = finalMetrics.jitterMs,
                        packetLossPercent = finalMetrics.packetLossPercent,
                        loadedPingMs = max(finalMetrics.loadedPingDownloadMs, finalMetrics.loadedPingUploadMs),
                        bufferbloatGrade = finalMetrics.bufferbloatGrade.grade,
                        ispName = _ispDetails.value.ispName,
                        publicIp = _ispDetails.value.publicIp,
                        serverName = _selectedServer.value.name,
                        serverCountry = _selectedServer.value.country,
                        networkType = _networkType.value.name,
                        wifiSsid = if (_networkType.value == NetworkType.WIFI) _wifiDetails.value.ssid else "",
                        wifiRssiDbm = if (_networkType.value == NetworkType.WIFI) _wifiDetails.value.rssiDbm else 0,
                        isRealUnthrottled = true
                    )
                    repository.insertResult(resultEntity)
                }
            } catch (e: Exception) {
                _testStage.value = TestStage.ERROR
            }
        }
    }

    fun cancelTest() {
        speedEngine.cancel()
        activeTestJob?.cancel()
        _testStage.value = TestStage.IDLE
        _speedMetrics.value = SpeedMetrics()
    }

    fun deleteHistoryItem(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteResult(id)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearHistory()
        }
    }
}
