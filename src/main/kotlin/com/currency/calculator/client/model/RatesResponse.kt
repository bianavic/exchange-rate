package com.currency.calculator.client.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RatesResponse(
    @SerialName("BRL")
    var BRL: Double,
    @SerialName("EUR")
    var EUR: Double,
    @SerialName("INR")
    var INR: Double,
    @SerialName("USD")
    var USD: Double
)