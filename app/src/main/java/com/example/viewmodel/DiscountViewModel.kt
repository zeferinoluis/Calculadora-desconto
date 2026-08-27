package com.example.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

data class SimpleDiscountResult(
    val originalPrice: Double = 0.0,
    val finalPrice: Double = 0.0,
    val discountAmount: Double = 0.0,
    val discountPercent: Double = 0.0,
    val isValid: Boolean = false,
    val isIncrease: Boolean = false,
    val description: String = "Insira os dois valores"
) {
    companion object {
        private val symbols = DecimalFormatSymbols(Locale("pt", "PT")).apply {
            decimalSeparator = ','
            groupingSeparator = '.'
        }
        private val currencyFormat = DecimalFormat("#,##0.00", symbols)
        private val percentFormat = DecimalFormat("0.00", symbols)

        fun formatCurrency(value: Double): String = "${currencyFormat.format(value)} €"
        fun formatPercent(value: Double): String = "${percentFormat.format(value)}%"
    }

    val formattedPercent: String get() = formatPercent(kotlin.math.abs(discountPercent))
    val formattedDiscountAmount: String get() = formatCurrency(kotlin.math.abs(discountAmount))
}

class DiscountViewModel : ViewModel() {

    private val _originalPrice = MutableStateFlow("60")
    val originalPrice: StateFlow<String> = _originalPrice.asStateFlow()

    private val _finalPrice = MutableStateFlow("38")
    val finalPrice: StateFlow<String> = _finalPrice.asStateFlow()

    private val _result = MutableStateFlow(SimpleDiscountResult())
    val result: StateFlow<SimpleDiscountResult> = _result.asStateFlow()

    init {
        calculate()
    }

    fun onOriginalPriceChanged(value: String) {
        _originalPrice.value = sanitizeInput(value)
        calculate()
    }

    fun onFinalPriceChanged(value: String) {
        _finalPrice.value = sanitizeInput(value)
        calculate()
    }

    fun clear() {
        _originalPrice.value = ""
        _finalPrice.value = ""
        calculate()
    }

    private fun calculate() {
        val orig = parseDouble(_originalPrice.value)
        val final = parseDouble(_finalPrice.value)

        if (orig == null || final == null || orig <= 0) {
            _result.value = SimpleDiscountResult(
                isValid = false,
                description = "Insira os dois valores"
            )
            return
        }

        val discountAmount = orig - final
        val discountPct = ((orig - final) / orig) * 100.0
        val isIncrease = final > orig

        val desc = if (isIncrease) {
            "O valor final é superior ao original (aumento de ${SimpleDiscountResult.formatPercent(kotlin.math.abs(discountPct))})"
        } else {
            "de desconto sobre ${SimpleDiscountResult.formatCurrency(orig)} (poupança de ${SimpleDiscountResult.formatCurrency(discountAmount)})"
        }

        _result.value = SimpleDiscountResult(
            originalPrice = orig,
            finalPrice = final,
            discountAmount = discountAmount,
            discountPercent = discountPct,
            isValid = true,
            isIncrease = isIncrease,
            description = desc
        )
    }

    private fun sanitizeInput(input: String): String {
        return input.replace(',', '.')
            .filter { it.isDigit() || it == '.' }
            .let { text ->
                val firstDot = text.indexOf('.')
                if (firstDot != -1) {
                    text.substring(0, firstDot + 1) + text.substring(firstDot + 1).replace(".", "")
                } else {
                    text
                }
            }
    }

    private fun parseDouble(value: String): Double? {
        if (value.isBlank()) return null
        return try {
            value.replace(',', '.').toDouble()
        } catch (_: Exception) {
            null
        }
    }
}
