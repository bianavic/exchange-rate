package com.currency.calculator.product.entity

import jakarta.persistence.*

@Entity
@Table(name = "exchanges")
class Exchange() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    val id: Long = 0
    val conversion_rates: Double = 0.0
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(foreignKey = ForeignKey(name = "fk_exchange_product_id"))
    var product: Product? = null

    constructor(product: Product): this() {
        this.product = product
    }

}