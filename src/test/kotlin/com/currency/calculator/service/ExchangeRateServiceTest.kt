package com.currency.calculator.service

import com.currency.calculator.client.error.UnsupportedCodeException
import com.currency.calculator.client.feign.ExchangeFeignClient
import com.currency.calculator.client.model.ExchangeRatesResponse
import com.currency.calculator.client.model.RatesResponse
import com.currency.calculator.client.model.formatRatesToTwoDecimalPlaces
import com.currency.calculator.controller.ExchangeRateController
import com.currency.calculator.mock.RatesResponseMock
import com.google.gson.Gson
import io.mockk.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
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

    private val ratesResponseMock = RatesResponseMock()
    private val exchangeFeignClient = mockk<ExchangeFeignClient>()
    private val exchangeApiUrl = "https://v6.exchangerate-api.com/v6/1234"

    @Test
    fun `should return latest rates for a valid base code`() {

        val baseCode = "BRL"
        val expectedConversionRates = ratesResponseMock.getLatestMockRates()

        Mockito.`when`(exchangeRateService.getLatestByBaseCode(baseCode)).thenReturn(expectedConversionRates)

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
    fun `should calculate currency conversion`() {

        val amount = 529.99
        val baseCode = "BRL"
        val rates = ratesResponseMock.getLatestMockRates()

        every { exchangeFeignClient.getLatestExchangeFor(any(), any()) } returns getMockApiResponse(rates)
        val exchangeRateService = spyk(ExchangeRateServiceImpl(exchangeFeignClient, exchangeApiUrl))
        every { exchangeRateService.getLatestByBaseCode(baseCode) } returns rates
        val result = exchangeRateService.getAmountCalculated(amount)

        val expectedResponse = mapOf(
            "EUR" to (amount * rates.EUR),
            "USD" to (amount * rates.USD),
            "INR" to (amount * rates.INR)
        )
        val formattedExpectedResponse = expectedResponse.mapValues { (_, value) -> String.format("%.2f", value).toDouble() }

        assertEquals(formattedExpectedResponse, result)

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

        val expectedRatesResponse = exchangeRatesResponse.ratesResponse
        expectedRatesResponse.formatRatesToTwoDecimalPlaces(2)

        assertEquals(expectedRatesResponse, rates)
    }

    @Test
    fun `should format rates to two decimal places with scale 2`() {
        val ratesResponse = RatesResponse(
            BRL = 1.234567,
            EUR = 2.987654,
            INR = 3.6543,
            USD = 4.333333
        )

        ratesResponse.formatRatesToTwoDecimalPlaces(2)

        assertEquals(1.23, ratesResponse.BRL)
        assertEquals(2.99, ratesResponse.EUR)
        assertEquals(3.65, ratesResponse.INR)
        assertEquals(4.33, ratesResponse.USD)
    }

    @Test
    fun `should decode ExchangeRatesResponse correctly`() {

        val baseCode = "BRL"
        val apiUrlWithApiKey = "https://v6.exchangerate-api.com/v6/1234"
        val mockedResponse = getMockedResponse(baseCode)
        every { exchangeFeignClient.getLatestExchangeFor(apiUrlWithApiKey, baseCode) } returns mockedResponse

        val json = Json { ignoreUnknownKeys = true }

        val response = exchangeFeignClient.getLatestExchangeFor(apiUrlWithApiKey, baseCode)
        val exchangeRatesResponse = json.decodeFromString<ExchangeRatesResponse>(response)

        val result = exchangeRatesResponse.ratesResponse

        val expectedRatesResponse = ratesResponseMock.getLatestMockRates()
        assertEquals(expectedRatesResponse, result)
    }

    @Test
    fun `should throw UnsupportedCodeException when base code is not found`() {
        val invalidBaseCode = "XYZ"

        val exchangeRateServiceMock = mockk<ExchangeRateService>()
        every { exchangeRateServiceMock.getLatestByBaseCode(invalidBaseCode) } throws UnsupportedCodeException("Unsupported currency code: $invalidBaseCode")

        mockMvc.perform(get("/latest/$invalidBaseCode"))
            .andExpect(status().isNotFound)
    }


    @ParameterizedTest
    @ValueSource(strings = ["BRL", "EUR", "INR", "USD"])
    fun `test isValidBaseCode with valid base codes`(baseCode: String) {
        val isValid = isValidBaseCode(baseCode)
        assertEquals(true, isValid)
    }

    @ParameterizedTest
    @ValueSource(strings = ["ABC", "EEE", "XYZ", "123"])
    fun `test isValidBaseCode with invalid base codes`(baseCode: String) {
        assertThrows(UnsupportedCodeException::class.java) {
            isValidBaseCode(baseCode)
        }
    }

    @Test
    fun `should throw MalformedRequestException when amount is invalid`() {
        val invalidAmount = -100.0

        val result = mockMvc.perform(get("/calculate/$invalidAmount"))
            .andExpect(status().isBadRequest)
            .andReturn()

        val responseBody = result.response.contentAsString
        assertEquals("Malformed request: $invalidAmount", responseBody)
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

    private fun getMockedResponse(baseCode: String): String {
        val ratesResponse = ratesResponseMock.getLatestMockRates()
        val exchangeRatesResponse = ExchangeRatesResponse(baseCode, ratesResponse)
        return Json.encodeToString(ExchangeRatesResponse.serializer(), exchangeRatesResponse)
    }

}

private fun isValidBaseCode(baseCode: String): Boolean {
    val validBaseCodes = listOf("BRL", "EUR", "INR", "USD")
    if (!validBaseCodes.contains(baseCode)) {
        throw UnsupportedCodeException("Unsupported currency code: $baseCode")
    }
    return true
}