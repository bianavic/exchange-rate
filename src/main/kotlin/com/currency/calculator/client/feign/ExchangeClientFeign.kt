package com.currency.calculator.client.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "exchangeapi", url = "https://v6.exchangerate-api.com/v6/e8fd368fdb99836aaec5e272")
interface ExchangeClientFeign {

//    @GetMapping("/latest")
//    fun getLatest(): ExchangeRate

    @GetMapping("/latest/{baseCode}")
    fun getExchangeFromBRL(@PathVariable("baseCode") baseCode: String)

//    @GetMapping("/pair/{baseCode}/{targetCode}")
//    fun getExchangeFromBaseCodeToTargetCode(
//        @PathVariable("baseCode") baseCode: String,
//        @PathVariable("targetCode") targetCode: String
//    )

}

// https://v6.exchangerate-api.com/v6/e8fd368fdb99836aaec5e272/latest/USD

// e8fd368fdb99836aaec5e272