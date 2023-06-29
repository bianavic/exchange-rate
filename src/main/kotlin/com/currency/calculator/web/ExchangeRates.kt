package com.currency.calculator.web

data class ExchangeRates(
    val baseCurrency: String,
    val conversions: List<CurrencyConversion>
)
