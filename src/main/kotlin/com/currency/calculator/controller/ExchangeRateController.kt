package com.currency.calculator.controller

import com.currency.calculator.client.model.RatesResponse
import com.currency.calculator.service.ExchangeRateService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "Exchange Rate Controller", description = "RESTful API for managing rates.")
class ExchangeRateController(
    private val exchangeRateService: ExchangeRateService,
) {

    val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/latest/{baseCode}")
    @Operation(summary = "Get all registered rates by BASE CODE", description = "Retrieve a list of registered rates based on its BASE CODE (abbreviation)")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Operation successful"),
        ApiResponse(responseCode = "400", description = "Base Code not found"),
        ApiResponse(responseCode = "401", description = "Malformed request"),
        ApiResponse(responseCode = "403", description = "Inactive account"),
        ApiResponse(responseCode = "429", description = "API quota reached"),
        ApiResponse(responseCode = "404", description = "Base Code not found"),
    ])
    fun getLatestRatesFor(@PathVariable baseCode: String): ResponseEntity<RatesResponse> {

        val response = exchangeRateService.getLatestByBaseCode(baseCode)
        return if (response != null) ResponseEntity(response, HttpStatus.OK)
        else ResponseEntity(HttpStatus.NOT_FOUND)

    }

    @GetMapping("/calculate/{amount}")
    @Operation(summary = "Calculate the rate conversion based on AMOUNT", description = "Retrieve a conversion rate list of registered rates based on its AMOUNT")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Operation successful"),
        ApiResponse(responseCode = "400", description = "Malformed request")
    ])
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