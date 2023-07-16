package com.currency.calculator.service

import com.currency.calculator.client.exceptions.BaseCodeNotFoundException
import com.currency.calculator.client.feign.ExchangeFeignClient
import com.currency.calculator.client.model.RatesResponse
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

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
    
    @Test
    fun `should throw base code NOT FOUND exception for an invalid base code` () {

        val baseCode = "XYZ"

        val exchangeFeignClient = mockk<ExchangeFeignClient>()
        every { exchangeFeignClient.getLatestExchangeFor(baseCode) } throws Exception("Base code not found")

        val exchangeRateService = ExchangeRateService(exchangeFeignClient)

        val exception = assertThrows<BaseCodeNotFoundException> {
            exchangeRateService.getLatestByBaseCode(baseCode)
        }
        assertEquals("Base code $baseCode not found", exception.message)
    }

}