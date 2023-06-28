package com.currency.calculator.client

import kotlinx.serialization.Serializable

@Serializable
data class ConversionRatesResponse(
    var BRL: Double,
    val EUR: Double,
    val INR: Double,
    val USD: Double
)