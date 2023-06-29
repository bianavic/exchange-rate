package com.currency.calculator.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.springframework.stereotype.Component

@Serializable
//@Component
data class ExchangeRate(
    @SerialName("base_code")
    val base_code: String = "",
    @SerialName("conversion_rates")
    val conversion_rates: ConversionRatesResponse,
//    @SerialName("conversion_result")
//    val conversion_result: Double = 0.0
)