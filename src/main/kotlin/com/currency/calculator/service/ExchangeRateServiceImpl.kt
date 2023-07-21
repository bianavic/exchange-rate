package com.currency.calculator.service

import com.currency.calculator.client.exceptions.BaseCodeNotFoundException
import com.currency.calculator.client.exceptions.MalformedRequestException
import com.currency.calculator.client.feign.ExchangeFeignClient
import com.currency.calculator.client.model.ExchangeRatesResponse
import com.currency.calculator.client.model.RatesResponse
import com.currency.calculator.client.model.formatRatesResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service

@Service
class ExchangeRateServiceImpl(
    private val exchangeFeignClient: ExchangeFeignClient
    ) : ExchangeRateService {

    private val json = Json { ignoreUnknownKeys = true }

    lateinit var ratesResponse: RatesResponse

    override fun getLatestByBaseCode(baseCode: String): RatesResponse {

        try {
            val response = exchangeFeignClient.getLatestExchangeFor(baseCode)
            val exchangeRatesResponse = json.decodeFromString<ExchangeRatesResponse>(response)

            ratesResponse = exchangeRatesResponse.ratesResponse
            ratesResponse.formatRatesResponse(2) // Format the rates response

            return ratesResponse
        } catch (e: Exception) {
            throw BaseCodeNotFoundException("Base code $baseCode not found")
        }
    }

    override fun calculate(amount: Double): Map<String, Double> {

        if (amount <= 0.0) {
            throw MalformedRequestException("Invalid amount: $amount")
        }

        val conversionRates = getLatestByBaseCode("BRL")
        val convertAmounts = mutableMapOf<String, Double>()

        convertAmounts["EUR"] = amount * conversionRates.EUR
        convertAmounts["USD"] = amount * conversionRates.USD
        convertAmounts["INR"] = amount * conversionRates.INR

        return convertAmounts
    }

}