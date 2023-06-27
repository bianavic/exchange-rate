package com.currency.calculator.controller

import com.currency.calculator.client.ConversionRatesResponse
import com.currency.calculator.client.ExchangeRequest
import com.currency.calculator.product.entity.Exchange
import com.currency.calculator.product.entity.Product
import com.currency.calculator.product.service.ExchangeService
import com.currency.calculator.service.ExchangeRateService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.annotation.RequestScope
import java.math.BigDecimal

@RestController
class ExchangeRateController(
    @Autowired val exchangeRateService: ExchangeRateService,
    @Autowired val exchangeService: ExchangeService,
    private val requestContext: RequestContext
) {

    @GetMapping("/latest/{baseCode}")
    fun getLatestRatesFor(@PathVariable baseCode: String): ConversionRatesResponse {
        return exchangeRateService.getLatestByBaseCode(baseCode)
    }

    @PostMapping("/exchange")
    fun setExchange(@RequestBody(required = true) @Valid exchangeRequest: ExchangeRequest): Exchange? {
        val exchange = exchangeService.setForProduct(
            requestContext.product,
            exchangeRequest.currencyCode,
            BigDecimal(exchangeRequest.price).toDouble()
        )
        return exchange
    }

    @GetMapping("/price/{productPrice}")
    fun getExchange(@PathVariable productPrice: Double): Double? {
        val exchange = exchangeService.getForProduct(requestContext.product)
        return exchange?.conversion_rates?.times(productPrice)
    }
}

@Component
@RequestScope
class RequestContext(
    var product: Product = Product()
)