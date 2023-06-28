package com.currency.calculator.controller

import com.currency.calculator.client.ConversionRatesResponse
import com.currency.calculator.service.ExchangeRateService
import org.springframework.web.bind.annotation.*

@RestController
class ExchangeRateController(
    private val exchangeRateService: ExchangeRateService,
//    @Autowired val exchangeService: ExchangeService,
) {

    @GetMapping("/latest/{baseCode}")
    fun getLatestRatesFor(@PathVariable baseCode: String): ConversionRatesResponse {
        return exchangeRateService.getLatestByBaseCode(baseCode)
    }

    @GetMapping("/BRL/(productPrice)")
    fun convertProductPrice(@PathVariable("productPrice") productPrice: Double): Map<String, Double> {
        return exchangeRateService.getConvertedPrices(productPrice)
    }
}


    /**
     *POST
     * {
     *     "price": 529.99,
     *     "currencyCode": "BRL"
     * }
     *
     * CREATE
     * {
     *     "BRL": 1.0,
     *     "EUR": 0.1917,
     *     "INR": 17.1638,
     *     "USD": 0.2091
     * }
     *

     */
