package com.currency.calculator.controller

import com.currency.calculator.client.ConversionRatesResponse
import com.currency.calculator.service.ExchangeRateService
import com.currency.calculator.web.CurrencyConversion
import com.currency.calculator.web.ExchangeRates
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class ExchangeRateController(
    private val exchangeRateService: ExchangeRateService,
) {

    lateinit var exchangeRates: ExchangeRates
    lateinit var currencyConversion: CurrencyConversion

    @GetMapping("/latest/{baseCode}")
    fun getLatestRatesFor(@PathVariable baseCode: String): ConversionRatesResponse {
        return exchangeRateService.getLatestByBaseCode(baseCode)
    }

    // preciso corrigir o path pq este nao existe
    @GetMapping("latest/BRL/{productPrice}")
    fun convertProductPrice(@PathVariable productPrice: Double): Map<String, Double> {
        val exchangeRates = exchangeRateService.loadExchangeRates()
        val conversions = exchangeRates.conversions.associateBy { it.code }

        val convertedPrices = mutableMapOf<String, Double>()

        for (conversion in conversions.values) {
            convertedPrices[conversion.code] = productPrice * conversion.rate
        }
        return convertedPrices
    }

    @GetMapping("/calculate/{amount}")
    fun calculateCurrencyConversion(@PathVariable amount: Double): Map<String, Double> {

        val conversionRates = exchangeRateService.getLatestByBaseCode("BRL")

        val convertAmounts = mutableMapOf<String, Double>()

        convertAmounts["EUR"] = amount * conversionRates.EUR
        convertAmounts["USD"] = amount * conversionRates.USD

        return convertAmounts

    }

//    @GetMapping("/pair/conversion/{baseCode}/{targetCode}/{amount}")
//    fun getAmountConversion(
//        @PathVariable baseCode: String,
//        @PathVariable targetCode: String,
//        @PathVariable amount: Double): Double {
//        return exchangeRateService.getConversion(baseCode, targetCode, amount)
//    }

}