package com.currency.calculator.controller

import com.currency.calculator.client.model.ConversionRatesResponse
import com.currency.calculator.service.ExchangeRateService
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@AutoConfigureMockMvc
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class ExchangeRateControllerTest {

    companion object {
        private lateinit var wireMockServer: WireMockServer
        private const val baseCode = "BRL"

        @BeforeAll
        @JvmStatic
        fun init() {
            wireMockServer = WireMockServer(
                WireMockConfiguration().port(9999)
            )
            wireMockServer.start()
            WireMock.configureFor("localhost", 9999)
        }
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    @DisplayName("should get latest rates for BRL money")
    fun getLatestRatesFor() {

        val expectedResponse = """
            {
              "BRL": 1.0,
              "EUR": 0.186,
              "INR": 17.0755,
              "USD": 0.208
            }
        """.trimIndent()

        val request = MockMvcRequestBuilders.get("/latest/$baseCode")
        val response = mockMvc.perform(request).andReturn().response
        val actualJson = response.contentAsString

        JSONAssert.assertEquals(expectedResponse, actualJson, true)
    }

//    @Test
//    @DisplayName("should get NOT FOUND latest rates for BRL money")
//    fun getLatestRatesNotFoundForBaseCode() {
//
//        val baseCode = "AEI"
//
//        var expectedResponse = ConversionRatesResponse(1.0, 0.1858, 16.8273, 0.2043)
////        var expectedResponse = exchangeRateController.getLatestRatesFor(baseCode).body
//
//        var response = exchangeRateService.getLatestByBaseCode(baseCode)
//
//        Assertions.assertThrows(BaseCodeNotFoundException::class.java) {
//            exchangeRateService.getLatestByBaseCode(baseCode)
//        }
//    }

    @Test
    @DisplayName("should calculate currency conversion")
    fun calculateCurrencyConversion() {

        val amount = 529.99
        val baseCode = "BRL"
        val conversionRates = ConversionRatesResponse(1.0, 5.00, 16.00, 4.00)

        val expectedResponse = mapOf(
            "EUR" to (amount * conversionRates.EUR),
            "USD" to (amount * conversionRates.USD),
            "INR" to (amount * conversionRates.INR)
        )

        val exchangeRateService = mockk<ExchangeRateService>()
        every { exchangeRateService.getLatestByBaseCode(baseCode) } returns conversionRates

        val exchangeRateController = ExchangeRateController(exchangeRateService)

        val response = exchangeRateController.calculateCurrencyConversion(amount).body

        Assertions.assertEquals(response, expectedResponse)
    }

}