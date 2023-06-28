//package com.currency.calculator.product.service
//
//import com.currency.calculator.client.CurrencyCode
//import com.currency.calculator.product.entity.Exchange
//import com.currency.calculator.product.entity.Product
//import com.currency.calculator.product.repository.ExchangeRepository
//import org.springframework.stereotype.Service
//import org.springframework.transaction.annotation.Transactional
//
//@Service
//@Transactional
//class ExchangeService(private val exchangeRepository: ExchangeRepository) {
//
//    fun createForProduct(product: Product): Exchange {
//        val exchange = Exchange(product)
//        exchangeRepository.save(exchange)
//        return exchange
//    }
//
//    fun getForProduct(product: Product): Exchange? {
//        return exchangeRepository.findByProductPrice(product.price) ?: createForProduct(product)
//    }
//
//    fun setForProduct(product: Product, currencyCode: CurrencyCode, price: Double): Exchange? {
//        val exchange = exchangeRepository.findByProductPrice(product.price) ?: createForProduct(product)
//
//        when (currencyCode) {
//            CurrencyCode.BRL -> currencyCode.name
//            CurrencyCode.EUR -> currencyCode.name
//            CurrencyCode.INR -> currencyCode.name
//            CurrencyCode.USD -> currencyCode.name
//        }
//        return exchange
//    }
//
//}