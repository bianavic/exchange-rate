package com.currency.calculator.controller

import com.currency.calculator.client.ConversionRatesResponse
import com.currency.calculator.service.ExchangeRateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ExchangeRateController(@Autowired val exchangeRateService: ExchangeRateService) {

    @GetMapping("/latest/{baseCode}")
    fun getLatestRatesFor(@PathVariable baseCode: String): ConversionRatesResponse {
        return exchangeRateService.getLatestByBaseCode(baseCode)
    }

}