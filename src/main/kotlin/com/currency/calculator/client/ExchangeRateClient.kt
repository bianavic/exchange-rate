package com.currency.calculator.client

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRateClient(
    val base_code: String,
    val conversion_rates: ConversionRatesResponse
)
