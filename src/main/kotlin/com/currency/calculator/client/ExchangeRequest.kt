package com.currency.calculator.client

data class ExchangeRequest(
    val price: Double,
    val currencyCode: CurrencyCode
)
