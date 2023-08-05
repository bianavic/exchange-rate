package com.currency.calculator

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@OpenAPIDefinition(
    info = Info(
        title = "Currency Converter API",
        version = "1",
        description = "A service that convert currencies EUR, INR, USD to BRL based on a provided value and gets latest foreign exchange rates."
    )
)
@EnableFeignClients
@SpringBootApplication
@EnableWebMvc
class CalculatorApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<CalculatorApplication>(*args)
        }
    }

}