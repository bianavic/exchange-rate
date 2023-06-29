package com.currency.calculator.client.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRatesResponse(
    @SerialName("base_code")
    val base_code: String = "",
    @SerialName("conversion_rates")
    val conversion_rates: ConversionRatesResponse
)