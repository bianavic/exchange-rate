package com.currency.calculator.controller

import com.currency.calculator.client.model.ConversionRatesResponse
import com.currency.calculator.service.ExchangeRateService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class ExchangeRateControllerTest {

    @Autowired
    lateinit var exchangeRateService: ExchangeRateService

    lateinit var mockExchangeResponse: ConversionRatesResponse

    @Test
    @DisplayName("should get latest rates for BRL money")
    fun getLatestRatesFor() {

        val baseCode = "BRL"

        var expectedResponse = ConversionRatesResponse(1.0, 0.187, 16.9601, 0.2061)

        var response = exchangeRateService.getLatestByBaseCode(baseCode)

        Assertions.assertEquals(response, expectedResponse)
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