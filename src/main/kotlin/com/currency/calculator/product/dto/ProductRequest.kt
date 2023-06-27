package com.currency.calculator.product.dto

import java.math.BigDecimal

data class ProductRequest(
    val name: String = "",
    var price: BigDecimal = BigDecimal.ZERO,
)
