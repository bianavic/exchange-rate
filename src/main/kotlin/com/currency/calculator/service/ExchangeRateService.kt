package com.currency.calculator.service

import com.currency.calculator.client.ConversionRatesResponse
import com.currency.calculator.client.ExchangeRateClient
import com.currency.calculator.client.feign.ExchangeClientFeign
import com.currency.calculator.product.repository.ExchangeRepository
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ExchangeRateService(
    @Autowired private val exchangeClientFeign: ExchangeClientFeign,
    @Autowired private val exchangeRepository: ExchangeRepository,
) {

    private val json = Json { ignoreUnknownKeys = true }

    fun getLatestByBaseCode(baseCode: String): ConversionRatesResponse {
        val exchangeRateResponse = exchangeClientFeign.getLatestExchangeFor(baseCode)
        return json.decodeFromString<ExchangeRateClient>(exchangeRateResponse).conversion_rates
    }

}