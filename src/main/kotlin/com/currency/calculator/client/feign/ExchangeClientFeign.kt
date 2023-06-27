package com.currency.calculator.client.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "exchangeapi", url = "https://v6.exchangerate-api.com/v6/e8fd368fdb99836aaec5e272")
interface ExchangeClientFeign {

    @GetMapping("/latest/{baseCode}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun getExchangeFromBRL(
        @PathVariable("baseCode") baseCode: String): String

}