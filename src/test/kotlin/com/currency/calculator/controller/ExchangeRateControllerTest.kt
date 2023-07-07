package com.currency.calculator.controller

import com.currency.calculator.client.model.ConversionRatesResponse
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.google.gson.Gson
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
internal class ExchangeRateControllerTest {

    private val gson = Gson()

    @Autowired
    lateinit var exchangeRateController: ExchangeRateController

    var wireMockServer = WireMockServer(9999)

    @BeforeEach
    fun setup(){
        wireMockServer.start()
    }

    @AfterEach
    fun close(){
        wireMockServer.stop()
    }

    @Test
    @DisplayName("should get latest rates for BRL money")
    fun getLatestRatesFor() {
        var resultExpected = ConversionRatesResponse(1.0, 0.1892, 17.0002, 0.2061)
        val baseCode = "BRL"

        wireMockServer.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/latest/$baseCode"))
                .willReturn(
                    WireMock.aResponse()
                        .withBody(gson.toJson(resultExpected))
                        .withHeader("Content-Type", "application/json")))

        var result = exchangeRateController.getLatestRatesFor(baseCode)

        Assertions.assertEquals(result, resultExpected)
    }

//    @Test
//    fun `should calculate currency conversion`() {
//        // Arrange
//        val amount = 529.99
//        val baseCode = "BRL"
//        val conversionRates = ConversionRatesResponse(1.0, 5.278, 16.90660, 4.85)
//        every { exchangeRateService.getLatestByBaseCode(baseCode) } returns conversionRates
//
//        // Act
//        val result = mockMvc.perform(MockMvcRequestBuilders.get("/calculate/{amount}", amount))
//            .andExpect(MockMvcResultMatchers.status().isOk)
//            .andReturn()
//
//        // Assert
//        val responseType = object : TypeToken<Map<String, Double>>() {}.type
//        val response = gson.fromJson<Map<String, Double>>(result.response.contentAsString, responseType)
//        response shouldBe mapOf(
//            "EUR" to (amount * conversionRates.EUR),
//            "USD" to (amount * conversionRates.USD),
//            "INR" to (amount * conversionRates.INR)
//        )
//    }

}