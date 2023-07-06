package com.currency.calculator.controller

import com.currency.calculator.client.model.ConversionRatesResponse
import com.currency.calculator.service.ExchangeRateService
import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


@SpringBootTest
@ExtendWith(MockKExtension::class, WireMockExtension::class)
@AutoConfigureMockMvc
internal class ExchangeRateControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var exchangeRateService: ExchangeRateService

    private val gson = Gson()

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should return latest rates for a base code`() {
        // Arrange
        val baseCode = "BRL"
        val conversionRatesResponse = ConversionRatesResponse(1.0, 5.278, 16.90660, 4.85)
        every { exchangeRateService.getLatestByBaseCode(baseCode) } returns conversionRatesResponse

        // Act
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/latest/{baseCode}", baseCode))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        // Assert
        val response = gson.fromJson(result.response.contentAsString, ConversionRatesResponse::class.java)
        response shouldBe conversionRatesResponse
    }

    @Test
    fun `should calculate currency conversion`() {
        // Arrange
        val amount = 529.99
        val baseCode = "BRL"
        val conversionRates = ConversionRatesResponse(1.0, 5.278, 16.90660, 4.85)
        every { exchangeRateService.getLatestByBaseCode(baseCode) } returns conversionRates

        // Act
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/calculate/{amount}", amount))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        // Assert
        val responseType = object : TypeToken<Map<String, Double>>() {}.type
        val response = gson.fromJson<Map<String, Double>>(result.response.contentAsString, responseType)
        response shouldBe mapOf(
            "EUR" to (amount * conversionRates.EUR),
            "USD" to (amount * conversionRates.USD),
            "INR" to (amount * conversionRates.INR)
        )
    }

}