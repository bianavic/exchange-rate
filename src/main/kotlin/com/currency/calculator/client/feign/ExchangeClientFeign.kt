package com.currency.calculator.client.feign

import com.currency.calculator.web.ExchangeRates
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

// FeignClient
@FeignClient(name = "exchangeapi", url = "\${exchange.url}")
interface ExchangeClientFeign {

    @GetMapping("/latest/{baseCode}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun getLatestExchangeFor(
        @PathVariable("baseCode") baseCode: String): String

    @GetMapping("/BRL/{baseCode}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun getExchangeFromBRL(): ExchangeRates

//    @GetMapping("/pair/{baseCode}/{targetCode}/{amount}", consumes = [MediaType.APPLICATION_JSON_VALUE])
//    fun getAmountConversion(
//        @PathVariable baseCode: String,
//        @PathVariable targetCode: String,
//        @PathVariable amount: Double): Double

}