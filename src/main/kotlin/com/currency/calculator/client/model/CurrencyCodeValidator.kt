package com.currency.calculator.client.model

object CurrencyCodeValidator {

    fun isValidBaseCode(baseCode: String): Boolean {
        val validBaseCodes = listOf("BRL", "EUR", "INR", "USD")
        return validBaseCodes.contains(baseCode)
    }

}