package com.currency.calculator.client

import com.currency.calculator.client.feign.ExchangeClientFeign
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ExchangeRateService(@Autowired private val exchangeClientFeign: ExchangeClientFeign) {

//    fun getLatest(): ExchangeRate {
//        return exchangeClientFeign.getLatest()
//    }

    fun getByBRL(baseCode: String) {
        return exchangeClientFeign.getExchangeFromBRL(baseCode)
    }

//    fun getFromTo(baseCode: String, targetCode: String) {
//        return exchangeClientFeign.getExchangeFromBaseCodeToTargetCode(baseCode, targetCode)
//    }

}