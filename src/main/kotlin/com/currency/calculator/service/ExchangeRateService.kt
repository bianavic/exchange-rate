package com.currency.calculator.service

import com.currency.calculator.client.ConversionRatesResponse
import com.currency.calculator.client.ExchangeRate
import com.currency.calculator.client.ExchangeRateClient
import com.currency.calculator.client.feign.ExchangeClientFeign
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ExchangeRateService(
    @Autowired private val exchangeClientFeign: ExchangeClientFeign,
) {

    private val json = Json { ignoreUnknownKeys = true }

    fun getLatestByBaseCode(baseCode: String): ConversionRatesResponse {
        val exchangeRateResponse = exchangeClientFeign.getLatestExchangeFor(baseCode)
        return json.decodeFromString<ExchangeRateClient>(exchangeRateResponse).conversion_rates
    }

    fun getConvertedPrices(productPrice: Double): Map<String, Double> {
        val exchangeRates = getExchangeRatesFromBRL()
        val conversionRates = exchangeRates.conversionRates

        val convertedPrices = mutableMapOf<String, Double>()
        convertedPrices["USD"] = productPrice * conversionRates.USD
        convertedPrices["EUR"] = productPrice * conversionRates.EUR
        convertedPrices["INR"] = productPrice * conversionRates.INR

        return convertedPrices
    }

    fun getExchangeRatesFromBRL(): ExchangeRate {
        val baseCode = "BRL"
        return exchangeClientFeign.getExchangeFromBRL(baseCode)
    }

}