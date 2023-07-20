package com.currency.calculator

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.servers.Server
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@OpenAPIDefinition(servers = [Server(url = "/", description = "Default Server URL")])
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