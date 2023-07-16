package com.currency.calculator.mock

import com.currency.calculator.client.model.RatesResponse

class RatesResponseMock {

    fun getLatestRates(): RatesResponse {
        return RatesResponse(
            BRL = 1.0,
            EUR = 0.186,
            INR = 17.0755,
            USD = 0.208
        )
    }

}