package com.currency.calculator.controller

import com.currency.calculator.client.ConversionRatesResponse
import com.currency.calculator.service.ExchangeRateService
import org.springframework.web.bind.annotation.*

@RestController
class ExchangeRateController(
    private val exchangeRateService: ExchangeRateService,
) {

    @GetMapping("/latest/{baseCode}")
    fun getLatestRatesFor(@PathVariable baseCode: String): ConversionRatesResponse {

        return exchangeRateService.getLatestByBaseCode(baseCode)

    }

    @GetMapping("/calculate/{amount}")
    fun calculateCurrencyConversion(@PathVariable amount: Double): Map<String, Double> {

        val conversionRates = exchangeRateService.getLatestByBaseCode("BRL")

        val convertAmounts = mutableMapOf<String, Double>()

        convertAmounts["EUR"] = amount * conversionRates.EUR
        convertAmounts["USD"] = amount * conversionRates.USD
        convertAmounts["INR"] = amount * conversionRates.INR

        return convertAmounts

    }

}