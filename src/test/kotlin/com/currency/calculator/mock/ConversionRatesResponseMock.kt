package com.currency.calculator.mock

import com.currency.calculator.client.model.ConversionRatesResponse

class ConversionRatesResponseMock {

    fun getLatestRates(): ConversionRatesResponse {
        return ConversionRatesResponse(
            BRL = 1.0,
            EUR = 0.186,
            INR = 17.0755,
            USD = 0.208
        )
    }

}