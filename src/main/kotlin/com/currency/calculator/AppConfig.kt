package com.currency.calculator

import com.currency.calculator.client.ConversionRatesResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {

    @Bean
    fun conversionRatesResponse(): ConversionRatesResponse {
        return ConversionRatesResponse(0.0, 0.0, 0.0, 0.0)
    }

}