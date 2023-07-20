package com.currency.calculator.configuration

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun apiInfo() = OpenAPI().info(
        Info().title("Exchange Rate Service")
            .description("API to fetch exchange rate for a given product")
            .version("v0.0.1")
            .contact(contactDetails())
    )

    fun contactDetails() = Contact().name("bianavic")
}