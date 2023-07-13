package com.currency.calculator.controller

import com.currency.calculator.client.model.ConversionRatesResponse
import com.currency.calculator.service.ExchangeRateService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class ExchangeRateController(
    private val exchangeRateService: ExchangeRateService,
) {

    @GetMapping("/latest/{baseCode}")
    fun getLatestRatesFor(@PathVariable baseCode: String): ResponseEntity<ConversionRatesResponse> {

        val response = exchangeRateService.getLatestByBaseCode(baseCode)
        return if (response != null) ResponseEntity(response, HttpStatus.OK)
        else ResponseEntity(HttpStatus.NOT_FOUND)

    }

    @GetMapping("/calculate/{amount}")
    fun calculateCurrencyConversion(@PathVariable amount: Double): ResponseEntity<Map<String, Double>> {

        val conversionRates = exchangeRateService.getLatestByBaseCode("BRL")

        val convertAmounts = mutableMapOf<String, Double>()

        convertAmounts["EUR"] = amount * conversionRates.EUR
        convertAmounts["USD"] = amount * conversionRates.USD
        convertAmounts["INR"] = amount * conversionRates.INR

        return if (convertAmounts != null) ResponseEntity(convertAmounts, HttpStatus.OK)
        else ResponseEntity(HttpStatus.BAD_REQUEST)

    }

}