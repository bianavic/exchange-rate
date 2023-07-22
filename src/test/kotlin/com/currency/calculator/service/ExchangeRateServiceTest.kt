package com.currency.calculator.service

import com.currency.calculator.client.exceptions.BaseCodeNotFoundException
import com.currency.calculator.client.exceptions.MalformedRequestException
import com.currency.calculator.client.feign.ExchangeFeignClient
import com.currency.calculator.client.model.ExchangeRatesResponse
import com.currency.calculator.client.model.RatesResponse
import com.currency.calculator.client.model.formatRatesToTwoDecimalPlaces
import com.currency.calculator.controller.ExchangeRateController
import com.currency.calculator.mock.RatesResponseMock
import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ExchangeRateController::class)
@TestPropertySource("classpath:application-test.properties")
class ExchangeRateServiceTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var exchangeRateService: ExchangeRateService

    @Test
    fun `should return latest rates for a valid base code`() {
        // Arrange
        val baseCode = "BRL"
        val ratesResponseMock = RatesResponseMock()
        val expectedConversionRates = ratesResponseMock.getLatestRates()

        Mockito.`when`(exchangeRateService.getLatestByBaseCode(baseCode)).thenReturn(expectedConversionRates)

        // Act & Assert
        val result = mockMvc.perform(get("/latest/$baseCode"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        val actualResponse = result.response.contentAsString
        val gson = Gson()
        val actualRatesResponse = gson.fromJson(actualResponse, RatesResponse::class.java)

        assertEquals(expectedConversionRates, actualRatesResponse)
    }

    @Test
    fun `should throw BaseCodeNotFoundException when base code is not found` () {

        val invalidBaseCode = "INVALID_BASE_CODE"

        val exchangeFeignClient = mockk<ExchangeFeignClient>()
        val exchangeApiUrl = "https://v6.exchangerate-api.com/v6/\${EXCHANGE_API_KEY}"

        every { exchangeFeignClient.getLatestExchangeFor(any(), invalidBaseCode) } throws RuntimeException("Base code not found")

        val exchangeRateService = ExchangeRateServiceImpl(exchangeFeignClient, exchangeApiUrl)

        assertThrows<BaseCodeNotFoundException> {
            exchangeRateService.getLatestByBaseCode(invalidBaseCode)
        }
    }

    @Test
    fun `should calculate currency conversion`() {
        // Arrange
        val amount = 529.99
        val baseCode = "BRL"
        val ratesResponseMock = RatesResponseMock()
        val rates = ratesResponseMock.getLatestRates()

        // Create mock instances
        val exchangeFeignClient = mockk<ExchangeFeignClient>()
        val exchangeApiUrl = "https://v6.exchangerate-api.com/v6/test-api-key"

        // Set up the mock response
        every { exchangeFeignClient.getLatestExchangeFor(any(), any()) } returns getMockApiResponse(rates)

        // Create a partial mock of the ExchangeRateServiceImpl and pass the dependencies
        val exchangeRateService = spyk(ExchangeRateServiceImpl(exchangeFeignClient, exchangeApiUrl))

        // Set up the mock behavior
        every { exchangeRateService.getLatestByBaseCode(baseCode) } returns rates

        // Act
        val result = exchangeRateService.getAmountCalculated(amount)

        // Assert
        val expectedResponse = mapOf(
            "EUR" to (amount * rates.EUR),
            "USD" to (amount * rates.USD),
            "INR" to (amount * rates.INR)
        )

        val formattedExpectedResponse = expectedResponse.mapValues { (_, value) -> String.format("%.2f", value).toDouble() }

        assertEquals(formattedExpectedResponse, result)

        // Verify that decodeFromString is called with the correct argument
        val responseJsonString = """
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
        val json = Json { ignoreUnknownKeys = true }
        val exchangeRatesResponse = json.decodeFromString<ExchangeRatesResponse>(ExchangeRatesResponse.serializer(), responseJsonString)

        // Verify that formatRatesToTwoDecimalPlaces is called with the correct argument
        val expectedRatesResponse = exchangeRatesResponse.ratesResponse
        expectedRatesResponse.formatRatesToTwoDecimalPlaces(2)

        assertEquals(expectedRatesResponse, rates)
    }

    @Test
    fun `should throw MalformedRequestException when amount is invalid`() {
        val invalidAmount = -100.0

        Mockito.doThrow(MalformedRequestException("Invalid amount: $invalidAmount"))
            .`when`(exchangeRateService).getAmountCalculated(invalidAmount)

        mockMvc.perform(get("/calculate/$invalidAmount"))
            .andExpect(status().isBadRequest)
    }

    private fun getMockApiResponse(rates: RatesResponse): String {
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