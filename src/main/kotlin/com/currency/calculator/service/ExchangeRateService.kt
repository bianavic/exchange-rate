package com.currency.calculator.service

import com.currency.calculator.client.model.RatesResponse

interface ExchangeRateService {

    fun getLatestByBaseCode(baseCode: String): RatesResponse

    fun getAmountCalculated(amount: Double): Map<String, Double>

}