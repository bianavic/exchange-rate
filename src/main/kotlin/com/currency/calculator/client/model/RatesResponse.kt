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

fun RatesResponse.formatRatesToTwoDecimalPlaces(scale: Int) {
    BRL = BRL.format(scale).toDouble()
    EUR = EUR.format(scale).toDouble()
    INR = INR.format(scale).toDouble()
    USD = USD.format(scale).toDouble()
}

fun MutableMap<String, Double>.formatAmountToTwoDecimalPlaces(scale: Int) {
    this.forEach { (key, value) ->
        this[key] = value.format(scale).toDouble() }
}

fun Double.format(scale: Int) = "%.${scale}f".format(this)