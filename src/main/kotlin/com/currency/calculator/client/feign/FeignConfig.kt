package com.currency.calculator.client.feign

import com.currency.calculator.client.error.FeignErrorDecoder
import com.google.gson.Gson
import feign.Logger
import org.springframework.context.annotation.Bean

class FeignConfig {

    @Bean
    fun loggerLevel(): Logger.Level = Logger.Level.FULL

    @Bean
    fun feignErrorDecoder(gson: Gson): FeignErrorDecoder {
        return FeignErrorDecoder(gson)
    }
}