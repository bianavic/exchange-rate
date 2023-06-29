package com.currency.calculator.client

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ConversionRates
@Serializable
data class ConversionRatesResponse(
//    @Contextual
    @SerialName("BRL")
    var BRL: Double,
//    @Contextual
    @SerialName("EUR")
    var EUR: Double,
//    @Contextual
    @SerialName("INR")
    var INR: Double,
//    @Contextual
    @SerialName("USD")
    var USD: Double
)