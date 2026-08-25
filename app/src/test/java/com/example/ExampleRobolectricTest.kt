package com.example

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.HopStatus
import com.example.model.ServerHopResult
import com.example.model.ServerLocation
import com.example.model.SpeedMetrics
import com.example.model.TestStage
import com.example.network.ServerEndpoints
import com.example.viewmodel.SpeedTestViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("RealSpeed", appName)
  }

  @Test
  fun `verify multi country server endpoints available`() {
    val servers = ServerEndpoints.SERVERS_LIST
    assertTrue(servers.isNotEmpty())
    assertTrue(servers.size >= 5)
    assertNotNull(servers.find { it.countryCode == "SG" })
    assertNotNull(servers.find { it.countryCode == "DE" })
  }

  @Test
  fun `verify multi server hop model`() {
    val server = ServerEndpoints.CLOUDFLARE_ANYCAST
    val hop = ServerHopResult(
        server = server,
        pingMs = 45.2,
        jitterMs = 2.4,
        downloadMbps = 85.5,
        uploadMbps = 42.0,
        status = HopStatus.COMPLETED,
        stageDescription = "Completed"
    )
    assertEquals(HopStatus.COMPLETED, hop.status)
    assertEquals(85.5, hop.downloadMbps, 0.01)
  }

  @Test
  fun `verify ViewModel initial multi-server mode is active`() {
    val app = ApplicationProvider.getApplicationContext<Application>()
    val vm = SpeedTestViewModel(app)
    assertTrue(vm.isMultiServerMode.value)
    assertTrue(vm.isBengali.value)
  }
}

