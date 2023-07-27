package com.currency.calculator

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.web.servlet.config.annotation.EnableWebMvc

//@OpenAPIDefinition(servers = [Server(url = "/", description = "Default Server URL")])
@OpenAPIDefinition(info = Info(title = "Currency Conversor API", version = "1", description = "A service that converts currencies EUR, INR, USD to BRL based on a provided value."))
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