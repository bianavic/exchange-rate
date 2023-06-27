package com.currency.calculator.client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ExchangeRateController(@Autowired val exchangeRateService: ExchangeRateService) {

    @GetMapping("/latest/{baseCode}")
    fun getByBRL(@PathVariable baseCode: String): ConversionRates {
        return exchangeRateService.getByBRL(baseCode)
    }

}