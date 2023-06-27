package com.currency.calculator.product.service

import com.currency.calculator.product.entity.Product
import com.currency.calculator.product.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ProductService {

    @Autowired
    lateinit var productRepository: ProductRepository

    fun addProduct(product: Product): Product {
        return productRepository.save(product)
    }

    fun getByPrice(price: BigDecimal): Product {
        return productRepository.findByPrice(price).get()
    }

    fun getProductById(id: Long): Product {
        return productRepository.findProductById(id)
    }

}