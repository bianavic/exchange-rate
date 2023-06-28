package com.currency.calculator.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRate(
    @SerialName("base_code")
    val baseCode: String,
    @SerialName("conversion_rates")
    val conversionRates: ConversionRatesResponse
)
