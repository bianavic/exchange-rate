package com.currency.calculator.client.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
data class ExchangeRatesResponse(
    @SerialName("base_code")
    val baseCode: String,
    @SerialName("conversion_rates")
    var ratesResponse: RatesResponse
)

fun exchangeSerialization(response: String): ExchangeRatesResponse {
    val json = Json { ignoreUnknownKeys = true }
    return json.decodeFromString<ExchangeRatesResponse>(response)
}