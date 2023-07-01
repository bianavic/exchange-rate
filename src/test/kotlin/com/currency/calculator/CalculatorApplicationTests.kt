package com.currency.calculator

import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [WireMockServerConfig::class])
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CalculatorApplicationTests {

	@Test
	fun contextLoads() {
	}

}
