package com.currency.calculator.client.feign

import com.currency.calculator.client.error.FeignErrorDecoder
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(
    name = "exchangeapi",
    url = "\${exchange.url}",
    configuration = [FeignConfig::class, FeignErrorDecoder::class]
)
fun interface ExchangeFeignClient {

    @GetMapping("/latest/{baseCode}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun getLatestExchangeFor(
        @PathVariable("baseCode") baseCode: String
    ): String

}