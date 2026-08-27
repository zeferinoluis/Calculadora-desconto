package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.viewmodel.DiscountViewModel
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
  fun `read app_name string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Calculadora de Desconto", appName)
  }

  @Test
  fun `calculate discount from 60 euros to 38 euros`() {
    val viewModel = DiscountViewModel()
    viewModel.onOriginalPriceChanged("60")
    viewModel.onFinalPriceChanged("38")

    val result = viewModel.result.value
    assertTrue(result.isValid)
    assertEquals(60.0, result.originalPrice, 0.001)
    assertEquals(38.0, result.finalPrice, 0.001)
    assertEquals(22.0, result.discountAmount, 0.001)
    assertEquals(36.6666, result.discountPercent, 0.01)
  }

  @Test
  fun `test clear button functionality`() {
    val viewModel = DiscountViewModel()
    viewModel.onOriginalPriceChanged("60")
    viewModel.onFinalPriceChanged("38")
    viewModel.clear()

    val result = viewModel.result.value
    assertEquals("", viewModel.originalPrice.value)
    assertEquals("", viewModel.finalPrice.value)
    assertEquals(false, result.isValid)
  }
}
