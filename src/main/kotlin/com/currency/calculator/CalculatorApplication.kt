package com.currency.calculator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

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