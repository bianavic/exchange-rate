package com.currency.calculator.service

import com.currency.calculator.client.error.MalformedRequestException
import com.currency.calculator.client.feign.ExchangeFeignClient
import com.currency.calculator.client.model.ExchangeRatesResponse
import com.currency.calculator.client.model.RatesResponse
import com.currency.calculator.client.model.formatAmountToTwoDecimalPlaces
import com.currency.calculator.client.model.formatRatesToTwoDecimalPlaces
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ExchangeRateServiceImpl(
    private val exchangeFeignClient: ExchangeFeignClient,
    @Value("\${exchange.url}")
    private val exchangeApiUrl: String
) : ExchangeRateService {

    private val json = Json { ignoreUnknownKeys = true }

    lateinit var ratesResponse: RatesResponse
    override fun getLatestByBaseCode(baseCode: String): RatesResponse {

        val apiUrlWithApiKey = exchangeApiUrl.replace("\${EXCHANGE_API_KEY}", System.getenv("EXCHANGE_API_KEY"))

        val response = exchangeFeignClient.getLatestExchangeFor(apiUrlWithApiKey, baseCode)
        val exchangeRatesResponse = json.decodeFromString<ExchangeRatesResponse>(response)

        val ratesResponse = exchangeRatesResponse.ratesResponse
        ratesResponse.formatRatesToTwoDecimalPlaces(2)

        return ratesResponse
    }

    override fun getAmountCalculated(amount: Double): Map<String, Double> {

        if (amount <= 0) {
            throw MalformedRequestException("Invalid amount: $amount")
        }

        val conversionRates = getLatestByBaseCode("BRL")

        return calculate(amount, conversionRates)
    }

    private fun calculate(
        amount: Double,
        conversionRates: RatesResponse
    ): MutableMap<String, Double> {
        val convertAmounts = mutableMapOf<String, Double>()

        convertAmounts["EUR"] = amount * conversionRates.EUR
        convertAmounts["USD"] = amount * conversionRates.USD
        convertAmounts["INR"] = amount * conversionRates.INR

        convertAmounts.formatAmountToTwoDecimalPlaces(2)
        return convertAmounts
    }

}