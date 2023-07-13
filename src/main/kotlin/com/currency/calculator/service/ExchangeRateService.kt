package com.currency.calculator.service

import com.currency.calculator.client.error.BaseCodeNotFoundException
import com.currency.calculator.client.error.FeignHttpException
import com.currency.calculator.client.feign.ExchangeFeignClient
import com.currency.calculator.client.model.ConversionRatesResponse
import com.currency.calculator.client.model.ExchangeRatesResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class ExchangeRateService(
    private val exchangeFeignClient: ExchangeFeignClient,
) {

    private val json = Json { ignoreUnknownKeys = true }

    fun getLatestByBaseCode(baseCode: String): ConversionRatesResponse {

        try {
            val response = exchangeFeignClient.getLatestExchangeFor(baseCode)
            val exchangeRatesResponse = json.decodeFromString<ExchangeRatesResponse>(response)

            return exchangeRatesResponse.conversion_rates

        } catch (e: FeignHttpException) {
            if (e.status == HttpStatus.NOT_FOUND.value()) {
                throw BaseCodeNotFoundException("Base code $baseCode not found")
            }
            throw e
        }

    }

}