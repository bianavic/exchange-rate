package com.currency.calculator.client.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "exchangeapi", url = "\${exchange.url}")
interface ExchangeClientFeign {

    @GetMapping("/latest/{baseCode}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun getLatestExchangeFor(
        @PathVariable("baseCode") baseCode: String): String

}