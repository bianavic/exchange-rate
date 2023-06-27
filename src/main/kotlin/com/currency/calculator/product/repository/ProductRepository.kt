package com.currency.calculator.product.repository

import com.currency.calculator.product.entity.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.util.*

@Repository
interface ProductRepository: JpaRepository<Product, Long> {

    fun findByPrice(price: BigDecimal): Optional<Product>

}