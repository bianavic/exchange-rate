package com.currency.calculator.controller

import com.currency.calculator.client.error.ExchangeRateException
import com.currency.calculator.client.error.MalformedRequestException
import com.currency.calculator.client.error.UnsupportedCodeException
import com.currency.calculator.client.model.CurrencyCodeValidator
import com.currency.calculator.client.model.RatesResponse
import com.currency.calculator.service.ExchangeRateService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
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

    companion object {
        val logger = LoggerFactory.getLogger(ExchangeRateController::class.java)
    }

    @GetMapping("/latest/{baseCode}")
    @Operation(summary = "Get all exchange rates by BASE CODE", description = "Retrieve a list of latest rates by BASE CODE (abbreviation)")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Operation successful", content = [Content(mediaType = "application/json")]),
        ApiResponse(responseCode = "400", description = "Malformed request"),
        ApiResponse(responseCode = "401", description = "Invalid API key"),
        ApiResponse(responseCode = "403", description = "Inactive account"),
        ApiResponse(responseCode = "429", description = "API quota reached"),
        ApiResponse(responseCode = "404", description = "Base Code not found"),
    ])
    fun getLatestRatesFor(@PathVariable baseCode: String): ResponseEntity<RatesResponse> {
        logger.info("getting exchange rate by baseCode: {}", baseCode)
        return try {
            if (!CurrencyCodeValidator.isValidBaseCode(baseCode)) {
                throw UnsupportedCodeException("Unsupported currency code: $baseCode")
            }
            val response = exchangeRateService.getLatestByBaseCode(baseCode)
            ResponseEntity(response, HttpStatus.OK)
        } catch (e: UnsupportedCodeException) {
            ResponseEntity.notFound().build()
        } catch (e: ExchangeRateException) {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("/calculate/{amount}")
    @Operation(
        summary = "Calculate the rate conversion based on AMOUNT",
        description = "Retrieve a list of currency conversion based on its AMOUNT")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Operation successful", content = [Content(mediaType = "application/json")]),
        ApiResponse(responseCode = "400", description = "Invalid Amount")
    ])
    fun calculateCurrencyConversion(@PathVariable amount: Double): ResponseEntity<Any> {
        logger.info("Calculating currency conversion based on amount {}", amount)
        return try {
            if (amount <= 0.0) {
                throw MalformedRequestException("Malformed request: $amount")
            }
            val convertAmounts = exchangeRateService.getAmountCalculated(amount)
            ResponseEntity(convertAmounts, HttpStatus.OK)
        } catch (e: ExchangeRateException) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

}