package com.currency.calculator.client

import kotlinx.serialization.Serializable

// ConversionRates
@Serializable
data class ConversionRatesResponse(
    var BRL: Double,
    var EUR: Double,
    var INR: Double,
    var USD: Double
)