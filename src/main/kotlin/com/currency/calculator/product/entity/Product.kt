package com.currency.calculator.product.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.math.BigDecimal

@Entity
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @Column(nullable = false)
    val name: String = "",
    @Column(nullable = false)
    var price: BigDecimal = BigDecimal.ZERO,
    @Column(nullable = true)
    val quantity: Long = 0
)
