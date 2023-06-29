package com.currency.calculator.service

import com.currency.calculator.client.ConversionRatesResponse
import com.currency.calculator.client.ExchangeRate
import com.currency.calculator.client.feign.ExchangeClientFeign
import com.currency.calculator.web.CurrencyConversion
import com.currency.calculator.web.ExchangeRates
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service

@Service
//@Transactional
class ExchangeRateService(
    private val exchangeClientFeign: ExchangeClientFeign,
) {

    private val json = Json { ignoreUnknownKeys = true }

    fun getLatestByBaseCode(baseCode: String): ConversionRatesResponse {
        val exchangeRateResponse = exchangeClientFeign.getLatestExchangeFor(baseCode)
        val exchangeRate = json.decodeFromString<ExchangeRate>(exchangeRateResponse)

        return exchangeRate.conversion_rates
    }

    fun getExchangeRatesFromBRL(): ExchangeRates {
        return exchangeClientFeign.getExchangeFromBRL()
        //        return json.decodeFromString<ExchangeRate>(exchangeRateResponse).conversion_rates
    }

    private fun getExchangeRates(baseCode: String): ExchangeRate {
        val json = exchangeClientFeign.getLatestExchangeFor(baseCode)
        return Json.decodeFromString<ExchangeRate>(json)
    }

    fun loadExchangeRates(): ExchangeRates {
        val response = exchangeClientFeign.getExchangeFromBRL()

        if (response != null) {
            val conversions = mutableListOf<CurrencyConversion>()

            for ((code, rate) in response.conversions) {
                conversions.add(CurrencyConversion(code, rate))
            }

            return ExchangeRates(response.baseCurrency, conversions)
        }

        return ExchangeRates("", emptyList())
    }

}