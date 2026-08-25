package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactSupport
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.DeveloperSupportDialog
import com.example.ui.components.WelcomeNotificationDialog
import com.example.ui.screens.DeveloperSupportScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.NetworkRadarScreen
import com.example.ui.screens.ServersScreen
import com.example.ui.screens.SpeedTestScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.viewmodel.SpeedTestViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: SpeedTestViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val testStage by viewModel.testStage.collectAsStateWithLifecycle()
                val speedMetrics by viewModel.speedMetrics.collectAsStateWithLifecycle()
                val networkType by viewModel.networkType.collectAsStateWithLifecycle()
                val ispDetails by viewModel.ispDetails.collectAsStateWithLifecycle()
                val wifiDetails by viewModel.wifiDetails.collectAsStateWithLifecycle()
                val selectedServer by viewModel.selectedServer.collectAsStateWithLifecycle()
                val serversList by viewModel.serversList.collectAsStateWithLifecycle()
                val isMultiServerMode by viewModel.isMultiServerMode.collectAsStateWithLifecycle()

                val historyResults by viewModel.historyResults.collectAsStateWithLifecycle()
                val averageDownload by viewModel.averageDownload.collectAsStateWithLifecycle()
                val maxDownload by viewModel.maxDownload.collectAsStateWithLifecycle()

                var currentTab by remember { mutableIntStateOf(0) }
                var showSupportDialog by remember { mutableStateOf(false) }
                var isAppLoading by remember { mutableStateOf(true) }
                var showWelcomeDialog by remember { mutableStateOf(false) }

                if (isAppLoading) {
                    SplashScreen(
                        onLoaded = {
                            isAppLoading = false
                            showWelcomeDialog = true
                        }
                    )
                } else {
                    if (showWelcomeDialog) {
                        WelcomeNotificationDialog(onDismissRequest = { showWelcomeDialog = false })
                    }

                    if (showSupportDialog) {
                        DeveloperSupportDialog(onDismissRequest = { showSupportDialog = false })
                    }

                    Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .background(NeonCyan.copy(alpha = 0.2f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Speed,
                                            contentDescription = "App Logo",
                                            tint = NeonCyan,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "RealSpeed",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 18.sp,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .background(NeonCyan.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "GIGABIT",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            color = NeonCyan
                                        )
                                    }
                                }
                            },
                            actions = {
                                // Developer Support Action Button
                                Box(
                                    modifier = Modifier
                                        .padding(end = 12.dp)
                                        .background(CyberSurface, RoundedCornerShape(10.dp))
                                        .border(1.dp, CyberCardBorder, RoundedCornerShape(10.dp))
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    IconButton(
                                        onClick = { currentTab = 4 },
                                        modifier = Modifier
                                            .height(32.dp)
                                            .testTag("developer_support_button")
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.ContactSupport,
                                                contentDescription = "Developer Support",
                                                tint = NeonCyan,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "Support",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary
                                            )
                                        }
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = CyberBackground,
                                titleContentColor = TextPrimary
                            )
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            modifier = Modifier
                                .navigationBarsPadding()
                                .height(64.dp)
                                .testTag("main_bottom_nav"),
                            containerColor = CyberSurface,
                            tonalElevation = 8.dp
                        ) {
                            val items = listOf(
                                NavItem(
                                    title = "Speed Test",
                                    icon = Icons.Default.Speed,
                                    tag = "nav_speed_test"
                                ),
                                NavItem(
                                    title = "ISP Radar",
                                    icon = Icons.Default.Radar,
                                    tag = "nav_isp_radar"
                                ),
                                NavItem(
                                    title = "Servers",
                                    icon = Icons.Default.Public,
                                    tag = "nav_servers"
                                ),
                                NavItem(
                                    title = "History",
                                    icon = Icons.Default.History,
                                    tag = "nav_history"
                                ),
                                NavItem(
                                    title = "Support",
                                    icon = Icons.Default.SupportAgent,
                                    tag = "nav_support"
                                )
                            )

                            items.forEachIndexed { index, item ->
                                val selected = currentTab == index
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = { currentTab = index },
                                    icon = {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = item.title,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = item.title,
                                            fontSize = 9.sp,
                                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                            maxLines = 1
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = NeonCyan,
                                        selectedTextColor = NeonCyan,
                                        unselectedIconColor = TextMuted,
                                        unselectedTextColor = TextMuted,
                                        indicatorColor = CyberBackground
                                    ),
                                    modifier = Modifier.testTag(item.tag)
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        AnimatedContent(
                            targetState = currentTab,
                            transitionSpec = {
                                if (targetState > initialState) {
                                    (slideInHorizontally(tween(300)) { width -> width / 3 } + fadeIn(tween(300)))
                                        .togetherWith(slideOutHorizontally(tween(250)) { width -> -width / 3 } + fadeOut(tween(200)))
                                } else {
                                    (slideInHorizontally(tween(300)) { width -> -width / 3 } + fadeIn(tween(300)))
                                        .togetherWith(slideOutHorizontally(tween(250)) { width -> width / 3 } + fadeOut(tween(200)))
                                }
                            },
                            label = "tabTransition"
                        ) { tab ->
                            when (tab) {
                                0 -> SpeedTestScreen(
                                    testStage = testStage,
                                    speedMetrics = speedMetrics,
                                    networkType = networkType,
                                    ispDetails = ispDetails,
                                    wifiDetails = wifiDetails,
                                    selectedServer = selectedServer,
                                    isMultiServerMode = isMultiServerMode,
                                    onToggleMultiServerMode = { viewModel.toggleMultiServerMode() },
                                    onStartTest = { viewModel.startSpeedTest() },
                                    onCancelTest = { viewModel.cancelTest() },
                                    onSelectServerClick = { currentTab = 2 }
                                )
                                1 -> NetworkRadarScreen(
                                    networkType = networkType,
                                    ispDetails = ispDetails,
                                    wifiDetails = wifiDetails,
                                    onRefresh = { viewModel.refreshNetworkDetails() }
                                )
                                2 -> ServersScreen(
                                    serversList = serversList,
                                    selectedServer = selectedServer,
                                    onSelectServer = { server ->
                                        viewModel.selectServer(server)
                                        currentTab = 0
                                    },
                                    onPingAll = { viewModel.pingAllServers() }
                                )
                                3 -> HistoryScreen(
                                    results = historyResults,
                                    averageDownload = averageDownload,
                                    maxDownload = maxDownload,
                                    onDeleteItem = { id -> viewModel.deleteHistoryItem(id) },
                                    onClearAll = { viewModel.clearAllHistory() },
                                    onOpenSupport = { currentTab = 4 }
                                )
                                4 -> DeveloperSupportScreen(
                                    ispDetails = ispDetails,
                                    wifiDetails = wifiDetails
                                )
                            }
                        }
                    }
                }
                }
            }
        }
    }
}

data class NavItem(
    val title: String,
    val icon: ImageVector,
    val tag: String
)
