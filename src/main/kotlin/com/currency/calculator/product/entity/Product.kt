package com.currency.calculator.product.entity

import jakarta.persistence.*

@Entity
@Table(name = "products")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long = 0,
    @Column(nullable = false)
    val name: String = "",
    @Column(nullable = false)
    var price: Double = 1.0,
    @Column(nullable = true)
    val quantity: Long = 0,
)
