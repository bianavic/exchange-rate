package com.currency.calculator.service

import com.currency.calculator.client.ConversionRatesResponse
import com.currency.calculator.client.ExchangeRate
import com.currency.calculator.client.feign.ExchangeClientFeign
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service

@Service
class ExchangeRateService(
    private val exchangeClientFeign: ExchangeClientFeign,
) {

    private val json = Json { ignoreUnknownKeys = true }

    fun getLatestByBaseCode(baseCode: String): ConversionRatesResponse {
        val exchangeRateResponse = exchangeClientFeign.getLatestExchangeFor(baseCode)
        val exchangeRate = json.decodeFromString<ExchangeRate>(exchangeRateResponse)

        return exchangeRate.conversion_rates
    }

}