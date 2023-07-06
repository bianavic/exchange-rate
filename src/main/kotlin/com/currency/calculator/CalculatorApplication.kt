package com.currency.calculator

import feign.Logger
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean

@EnableFeignClients
@SpringBootApplication
class CalculatorApplication {

	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			runApplication<CalculatorApplication>(*args)
		}
	}
}

@Bean
fun feignLoggerLevel(): Logger.Level {
	return Logger.Level.FULL
}