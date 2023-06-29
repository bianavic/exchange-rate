package com.currency.calculator.service

import com.currency.calculator.client.model.ConversionRatesResponse
import com.currency.calculator.controller.ExchangeRateController
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class ExchangeRateServiceTest {

    private val exchangeRateService = Mockito.mock(ExchangeRateService::class.java)
    private var exchangeRateController = Mockito.mock(ExchangeRateController::class.java)
    private val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(exchangeRateController).build()

    @Test
    fun getLatestByBaseCode() {

        val baseCode = "BRL"
        val conversionRates = ConversionRatesResponse(1.0, 2.0, 3.0, 4.0)

        Mockito.`when`(exchangeRateService.getLatestByBaseCode(baseCode)).thenReturn(conversionRates)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/latest/$baseCode")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)

    }
}