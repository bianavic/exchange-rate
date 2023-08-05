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

fun parseJSON(jsonString: String): RatesResponse {
    val json = Json { ignoreUnknownKeys = true }
    val ratesResponse = json.decodeFromString<ExchangeRatesResponse>(jsonString).ratesResponse

    ratesResponse.BRL = ratesResponse.BRL ?: 0.0
    ratesResponse.EUR = ratesResponse.EUR ?: 0.0
    ratesResponse.INR = ratesResponse.INR ?: 0.0
    ratesResponse.USD = ratesResponse.USD ?: 0.0

    return ratesResponse
}