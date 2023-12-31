package com.currency.calculator.service

import com.currency.calculator.client.error.MalformedRequestException
import com.currency.calculator.client.feign.ExchangeFeignClient
import com.currency.calculator.client.model.*
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

    override fun getLatestByBaseCode(baseCode: String): RatesResponse {

        val exchangeApiResponse = exchangeFeignClient.getLatestExchangeFor(baseCode)
        val rates = parseJSON(exchangeApiResponse)

        rates.ratesResponse.formatRatesToTwoDecimalPlaces(DECIMAL_PLACES)

        logger.info("fetching exchange rates for base code: {}", baseCode)

        return rates.ratesResponse
    }

    override fun getAmountCalculated(amount: Double): Map<String, Double> {

        logger.info("getting amount: {}", amount)

        if (amount <= 0) {
            throw MalformedRequestException("Malformed request: $amount")
        }

        val conversionRates = getLatestByBaseCode("BRL")

        logger.info("fetching exchange rates {}", conversionRates)

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