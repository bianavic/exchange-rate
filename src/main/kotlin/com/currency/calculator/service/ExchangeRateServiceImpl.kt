package com.currency.calculator.service

import com.currency.calculator.client.error.MalformedRequestException
import com.currency.calculator.client.feign.ExchangeFeignClient
import com.currency.calculator.client.model.ExchangeRatesResponse
import com.currency.calculator.client.model.RatesResponse
import com.currency.calculator.client.model.formatAmountToTwoDecimalPlaces
import com.currency.calculator.client.model.formatRatesToTwoDecimalPlaces
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ExchangeRateServiceImpl(
    private val exchangeFeignClient: ExchangeFeignClient,
) : ExchangeRateService {

    companion object {
        private val logger = LoggerFactory.getLogger(ExchangeRateServiceImpl::class.java)
        private const val DECIMAL_PLACES = 2
    }

    private val json = Json { ignoreUnknownKeys = true }

    override fun getLatestByBaseCode(baseCode: String): RatesResponse {

        logger.info("getting base code: {}", baseCode)

        val response = exchangeFeignClient.getLatestExchangeFor(baseCode)

        val exchangeRatesResponse = json.decodeFromString<ExchangeRatesResponse>(response)
        val ratesResponse = exchangeRatesResponse.ratesResponse

        ratesResponse.formatRatesToTwoDecimalPlaces(DECIMAL_PLACES)

        logger.info("exchange rates fetched for base code: {}", baseCode)

        return ratesResponse
    }

    override fun getAmountCalculated(amount: Double): Map<String, Double> {

        logger.info("getting amount: {}", amount)

        if (amount <= 0) {
            throw MalformedRequestException("Malformed request: $amount")
        }

        val conversionRates = getLatestByBaseCode("BRL")

        logger.info("getting calculated converted amount")

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

        convertAmounts.formatAmountToTwoDecimalPlaces(DECIMAL_PLACES)

        return convertAmounts
    }

}