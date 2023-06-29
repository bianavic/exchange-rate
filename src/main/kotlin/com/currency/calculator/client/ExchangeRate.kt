package com.currency.calculator.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRate(
    @SerialName("base_code")
    val base_code: String = "",
    @SerialName("conversion_rates")
    val conversion_rates: ConversionRatesResponse
)