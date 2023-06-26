package com.currency.calculator.client

data class ExchangeRate(
    val baseCode: String,
    val conversionRates: List<ConversionRates>
)
