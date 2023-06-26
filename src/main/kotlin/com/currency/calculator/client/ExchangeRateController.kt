package com.currency.calculator.client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ExchangeRateController(@Autowired val exchangeRateService: ExchangeRateService) {

//    @GetMapping("/latest")
//    fun getLatest(): ExchangeRate {
//        return exchangeRateService.getLatest()
//    }

    @GetMapping("/latest/{baseCode}")
    fun getByBRL(@PathVariable baseCode: String) {
        return exchangeRateService.getByBRL(baseCode)
    }

//    @GetMapping("/pair/{baseCode}/{targetCode}")
//    fun getFromTo(
//        @PathVariable baseCode: String,
//        @PathVariable targetCode: String
//    ) {
//        return exchangeRateService.getFromTo(baseCode, targetCode)
//    }


// "https://v6.exchangerate-api.com/v6/e8fd368fdb99836aaec5e272/latest"

}