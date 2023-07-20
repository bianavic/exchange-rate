package com.currency.calculator.service

import com.currency.calculator.client.exceptions.BaseCodeNotFoundException
import com.currency.calculator.client.exceptions.MalformedRequestException
import com.currency.calculator.client.feign.ExchangeFeignClient
import com.currency.calculator.client.model.RatesResponse
import com.currency.calculator.controller.ExchangeRateController
import com.currency.calculator.mock.RatesResponseMock
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ExchangeRateController::class)
class ExchangeRateServiceTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var exchangeRateService: ExchangeRateService

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

        val exchangeRateService = ExchangeRateServiceImpl(exchangeFeignClient)

        // Act
        val result = exchangeRateService.getLatestByBaseCode(baseCode)

        // Assert
        assertEquals(expectedConversionRates, result)
    }

    @Test
    fun `should throw BaseCodeNotFoundException when base code is not found` () {
        val exchangeFeignClient = mockk<ExchangeFeignClient>()
        val exchangeRateService = ExchangeRateServiceImpl(exchangeFeignClient)

        val baseCode = "INVALID_BASE_CODE"
        every { exchangeFeignClient.getLatestExchangeFor(baseCode) } throws Exception("Base code not found")

        assertThrows<BaseCodeNotFoundException> {
            exchangeRateService.getLatestByBaseCode(baseCode)
        }
    }

    @Test
    fun `should calculate currency conversion`() {
        // Arrange
        val amount = 529.99
        val exchangeFeignClient = mockk<ExchangeFeignClient>()
        val exchangeRateService = ExchangeRateServiceImpl(exchangeFeignClient)

        // Stub getLatestByBaseCode
        val ratesResponseMock = RatesResponseMock()
        val rates = ratesResponseMock.getLatestRates()
        every { exchangeFeignClient.getLatestExchangeFor("BRL") } returns getMockApiResponse(rates)

        // Act
        val result = exchangeRateService.calculate(amount)

        // Assert
        val expectedResponse = mapOf(
            "EUR" to (amount * rates.EUR),
            "USD" to (amount * rates.USD),
            "INR" to (amount * rates.INR)
        )
        assertEquals(expectedResponse, result)
    }

    @Test
    fun `should throw MalformedRequestException when amount is invalid`() {
        val invalidAmount = -100.0

        Mockito.doThrow(MalformedRequestException("Invalid amount: $invalidAmount"))
            .`when`(exchangeRateService).calculate(invalidAmount)

        mockMvc.perform(get("/calculate/$invalidAmount"))
            .andExpect(status().isBadRequest)
    }

    private fun getMockApiResponse(rates: RatesResponse): String {
        // Return the mock API response as JSON
        return """
            {
                "base_code": "BRL",
                "conversion_rates": {
                    "BRL": 1.0,
                    "EUR": ${rates.EUR},
                    "USD": ${rates.USD},
                    "INR": ${rates.INR}
                }
            }
        """.trimIndent()
    }

}