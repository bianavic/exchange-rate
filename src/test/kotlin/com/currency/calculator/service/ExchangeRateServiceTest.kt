package com.currency.calculator.service

import com.currency.calculator.client.feign.ExchangeFeignClient
import com.currency.calculator.client.model.RatesResponse
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ExchangeRateServiceTest {

    @Test
    fun `should return latest rates for a valid base code`() {

// Arrange
        val baseCode = "BRL"
        val expectedConversionRates = RatesResponse(1.0, 5.0, 16.0, 4.0)
        val response = """
            {
                "base_code": "BRL",
                "conversion_rates": {
                    "BRL": 1.0,
                    "EUR": 5.0,
                    "USD": 4.0,
                    "INR": 16.0
                }
            }
        """

        val exchangeFeignClient = mockk<ExchangeFeignClient>()
        every { exchangeFeignClient.getLatestExchangeFor(baseCode) } returns response

        val exchangeRateService = ExchangeRateService(exchangeFeignClient)

        // Act
        val result = exchangeRateService.getLatestByBaseCode(baseCode)

        // Assert
        assertEquals(expectedConversionRates, result)
    }

}