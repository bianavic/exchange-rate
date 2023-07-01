package com.currency.calculator.controller

import com.currency.calculator.client.model.ConversionRatesResponse
import com.currency.calculator.service.ExchangeRateService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ExchangeRateController::class)
class ExchangeRateControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var exchangeRateService: ExchangeRateService

    // @ParameterizedTest
    @Test
    @DisplayName("Should Return Latest Rates for BRL")
    fun getLatestRatesFor() {

        val baseCode = "BRL"
        val conversionRates = ConversionRatesResponse(1.0, 5.278, 16.90660, 4.85)

        `when`(exchangeRateService.getLatestByBaseCode(baseCode))
            .thenReturn(conversionRates)

        mockMvc.perform(get("/latest/$baseCode")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)

    }

    // @ParameterizedTest
    @Test
    @DisplayName("Should Calculate Currency Conversion for BRL")
    fun calculateCurrencyConversion() {

        val amount = 529.99
        val conversionRates = ConversionRatesResponse(1.0, 5.278, 16.90660, 4.85 )
        val expectedConversion = mapOf(
            "EUR" to amount * conversionRates.EUR,
            "USD" to amount * conversionRates.USD,
            "INR" to amount * conversionRates.INR
        )

        `when`(exchangeRateService.getLatestByBaseCode("BRL")).thenReturn(conversionRates)

        mockMvc.perform(get("/calculate/{amount}", amount)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.EUR").value(expectedConversion["EUR"]))
            .andExpect(jsonPath("$.USD").value(expectedConversion["USD"]))
            .andExpect(jsonPath("$.INR").value(expectedConversion["INR"]))
    }

}