package com.currency.calculator.client

import com.currency.calculator.ExchangeRateResponse
import com.currency.calculator.client.feign.ExchangeClientFeign
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ExchangeRateService(
    @Autowired private val exchangeClientFeign: ExchangeClientFeign) {

    private val json = Json { ignoreUnknownKeys = true }

    fun getByBRL(baseCode: String): ConversionRates {
        val exchangeRateResponse = exchangeClientFeign.getExchangeFromBRL(baseCode)
        return json.decodeFromString<ExchangeRateResponse>(exchangeRateResponse).conversion_rates
    }

}