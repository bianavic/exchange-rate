package com.currency.calculator.service

import com.currency.calculator.client.exceptions.BaseCodeNotFoundException
import com.currency.calculator.client.feign.ExchangeFeignClient
import com.currency.calculator.client.model.ExchangeRatesResponse
import com.currency.calculator.client.model.RatesResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service

@Service
class ExchangeRateService(
    private val exchangeFeignClient: ExchangeFeignClient,
) {

    private val json = Json { ignoreUnknownKeys = true }

    fun getLatestByBaseCode(baseCode: String): RatesResponse {

        try {
            val response = exchangeFeignClient.getLatestExchangeFor(baseCode)
            val exchangeRatesResponse = json.decodeFromString<ExchangeRatesResponse>(response)
            return exchangeRatesResponse.ratesResponse
        } catch (e: Exception) {
            throw BaseCodeNotFoundException("Base code $baseCode not found")
        }
    }

}