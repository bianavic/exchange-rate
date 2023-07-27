package com.currency.calculator.controller

import com.currency.calculator.mock.RatesResponseMock
import com.currency.calculator.service.ExchangeRateService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@AutoConfigureMockMvc
@ExtendWith(SpringExtension::class)
@WebMvcTest(ExchangeRateController::class)
@TestPropertySource(properties = ["classpath:application-test.properties"])
internal class ExchangeRateControllerTest {

    companion object {
        private const val baseCode = "BRL"
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var exchangeRateService: ExchangeRateService

    @Test
    @DisplayName("should get latest rates for BRL money")
    fun getLatestRatesFor() {

        val ratesResponseMock = RatesResponseMock()
        val expectedResponse = """
            {
            "BRL": 1.0,
            "EUR": 0.18,
            "INR": 17.07,
            "USD": 0.20
            }
        """.trimIndent()

        val mockResponse = ratesResponseMock.getLatestMockRates()
        Mockito.`when`(exchangeRateService.getLatestByBaseCode(baseCode)).thenReturn(mockResponse)

        val request = MockMvcRequestBuilders.get("/latest/$baseCode")
        val response = mockMvc.perform(request).andReturn().response
        val actualJson = response.contentAsString

        JSONAssert.assertEquals(expectedResponse, actualJson, true)
    }

    @Test
    @DisplayName("should calculate currency conversion")
    fun calculateCurrencyConversion() {

        val amount = 529.99
        val ratesResponseMock = RatesResponseMock()
        val rates = ratesResponseMock.getLatestMockRates()

        val expectedResponse = mapOf(
            "EUR" to (amount * rates.EUR),
            "USD" to (amount * rates.USD),
            "INR" to (amount * rates.INR)
        )

        Mockito.`when`(exchangeRateService.getAmountCalculated(amount)).thenReturn(expectedResponse)

        val response = mockMvc.perform(MockMvcRequestBuilders.get("/calculate/$amount"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        val actualResponse = response.response.contentAsString

        val gson = Gson()
        val actualMapType = object : TypeToken<Map<String, Double>>() {}.type
        val formattedResponse: Map<String, Double> = gson.fromJson(actualResponse, actualMapType)

        JSONAssert.assertEquals(expectedResponse.toString(), actualResponse, true)

        assertEquals(expectedResponse, formattedResponse)
    }

}