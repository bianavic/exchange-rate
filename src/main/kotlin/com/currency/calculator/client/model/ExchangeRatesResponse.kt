package com.currency.calculator.client.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRatesResponse(
    @SerialName("base_code")
    val baseCode: String,
    @SerialName("conversion_rates")
    var ratesResponse: RatesResponse
)