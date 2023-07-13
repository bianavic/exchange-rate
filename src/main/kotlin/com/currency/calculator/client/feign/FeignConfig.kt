package com.currency.calculator.client.feign

import feign.Logger
import org.springframework.context.annotation.Bean

class FeignConfig {

    @Bean
    fun loggerLevel(): Logger.Level = Logger.Level.FULL

}