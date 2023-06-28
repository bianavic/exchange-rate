//package com.currency.calculator.product.mapper
//
//import com.currency.calculator.client.ExchangeResponse
//import com.currency.calculator.product.entity.Exchange
//
//class ExchangeProductMapper {
//    companion object {
//        fun toDTO(exchange: Exchange): ExchangeResponse? {
//
//            return ExchangeResponse(
//                id = exchange.id,
//                conversionRatesResponse = exchange.conversion_rates,
//                product = exchange.product!!
//            )
//        }
//    }
//}