package com.currency.calculator.product.repository

import com.currency.calculator.product.entity.Exchange
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExchangeRepository: JpaRepository<Exchange, Long> {

    fun findByProductId(productId: Long): Exchange?

    fun findByProductPrice(productPrice: Double): Exchange?

}