package com.currency.calculator.mock

import com.currency.calculator.client.model.RatesResponse

class RatesResponseMock {

    fun getLatestMockRates(): RatesResponse {
        return RatesResponse(
            BRL = 1.0,
            EUR = 0.18,
            INR = 17.07,
            USD = 0.20
        )
    }

}