package com.currency.calculator.client

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRate(
    val conversionRates: ConversionRates
)
