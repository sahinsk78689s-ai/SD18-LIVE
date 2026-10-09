package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("SD18 LIVE", appName)
  }

  @Test
  fun `verify twelve seats room configuration`() {
    val seats = com.example.data.MockDataProvider.create12Seats(8)
    assertEquals(12, seats.size)
    assertEquals(1, seats.first().seatNumber)
    assertEquals(12, seats.last().seatNumber)
  }

  @Test
  fun `verify demo coins catalog and agencies`() {
    val gifts = com.example.data.MockDataProvider.gifts
    assertTrue(gifts.isNotEmpty())
    assertTrue(gifts.all { it.coinPrice > 0 })

    val agencies = com.example.data.MockDataProvider.sampleAgencies
    assertTrue(agencies.isNotEmpty())
  }
}
