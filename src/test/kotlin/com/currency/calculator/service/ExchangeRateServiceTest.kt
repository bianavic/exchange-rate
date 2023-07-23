package com.currency.calculator.service

import com.currency.calculator.client.error.BaseCodeNotFoundException
import com.currency.calculator.client.error.ExchangeErrorDecoder
import com.currency.calculator.client.feign.ExchangeFeignClient
import com.currency.calculator.client.model.ExchangeRatesResponse
import com.currency.calculator.client.model.RatesResponse
import com.currency.calculator.client.model.formatRatesToTwoDecimalPlaces
import com.currency.calculator.controller.ExchangeRateController
import com.currency.calculator.mock.RatesResponseMock
import com.google.gson.Gson
import feign.Response
import io.mockk.*
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
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

    private val ratesResponseMock = RatesResponseMock()

    @MockBean
    private lateinit var exchangeRateService: ExchangeRateService

    // Create mock instances
    val exchangeFeignClient = mockk<ExchangeFeignClient>()
    val exchangeApiUrl = "https://v6.exchangerate-api.com/v6/test-api-key"

    @Test
    fun `should return latest rates for a valid base code`() {

        val baseCode = "BRL"
        val expectedConversionRates = ratesResponseMock.getLatestRates()

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
    fun `should throw BaseCodeNotFoundException when base code is not found`() {

        val methodKey = "ExchangeFeignClient#getLatestExchangeFor"
        val response = createMockResponse(HttpStatus.NOT_FOUND.value())
        val errorDecoder = ExchangeErrorDecoder()

        val exception = assertThrows<BaseCodeNotFoundException> {
            errorDecoder.decode(methodKey, response)
        }
        assertEquals("defaultBaseCode", exception.message)
    }

    @Test
    fun `should calculate currency conversion`() {

        val amount = 529.99
        val baseCode = "BRL"
        val rates = ratesResponseMock.getLatestRates()

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
    fun `test formatRatesToTwoDecimalPlaces with scale 2`() {

        val exchangeFeignClient = mockk<ExchangeFeignClient>()
        val exchangeApiUrl = "https://v6.exchangerate-api.com/v6/test-api-key"
        val json = Json { ignoreUnknownKeys = true }

        val ratesResponse = RatesResponse(1.234567, 2.987654, 3.0, 4.9999)
        val exchangeRatesResponse = ExchangeRatesResponse("BRL", ratesResponse)
        val responseJson = json.encodeToString(ExchangeRatesResponse.serializer(), exchangeRatesResponse)

        every {
            exchangeFeignClient.getLatestExchangeFor(any(), any())
        } returns responseJson

        val exchangeRateService = ExchangeRateServiceImpl(exchangeFeignClient, exchangeApiUrl)

        val result = exchangeRateService.getLatestByBaseCode("BRL")

        assertEquals(1.23, result.BRL)
        assertEquals(2.99, result.EUR)
        assertEquals(3.0, result.INR)
        assertEquals(5.0, result.USD)
    }

    @Test
    fun `should throw MalformedRequestException when amount is invalid`() {

        val invalidAmount = -100.0

        mockMvc.perform(get("/calculate/$invalidAmount"))
            .andExpect(status().isBadRequest)
            .andExpect(content().string("Invalid amount: -100.0"))
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

    private fun createMockResponse(statusCode: Int): Response {
        val response = mockk<Response>()
        every { response.status() } returns statusCode
        return response
    }

}