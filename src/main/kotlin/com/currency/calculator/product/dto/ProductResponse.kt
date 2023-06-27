package com.currency.calculator.product.dto

import com.currency.calculator.client.ConversionRatesResponse

data class ProductResponse(
    var price: Double,
    val productConversionRate: ConversionRatesResponse
)
