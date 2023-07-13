package com.currency.calculator.client.feign

import com.currency.calculator.client.error.BaseCodeNotFoundException
import com.currency.calculator.client.error.ExchangeFeignClientExceptionHandler
import com.currency.calculator.client.error.HandleFeignError
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import kotlin.jvm.Throws

@FeignClient(
    name = "exchangeapi",
    url = "\${exchange.url}",
    configuration = [FeignConfig::class]
) interface ExchangeFeignClient {

    @GetMapping("/latest/{baseCode}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @HandleFeignError(ExchangeFeignClientExceptionHandler::class)
    @Throws(BaseCodeNotFoundException::class)
    fun getLatestExchangeFor(
        @PathVariable("baseCode") baseCode: String): String

}