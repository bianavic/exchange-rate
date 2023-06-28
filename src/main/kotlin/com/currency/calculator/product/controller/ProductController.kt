package com.currency.calculator.product.controller

import com.currency.calculator.product.entity.Product
import com.currency.calculator.product.service.ProductService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/products")
class ProductController {

    @Autowired
    lateinit var productService: ProductService

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun add(@Valid @RequestBody product: Product): Product {
        return productService.addProduct(product)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): Product {
        return productService.getProductById(id)
    }

//    @GetMapping("/{price}")
//    fun getByPrice(@PathVariable price: BigDecimal): Product {
//        return productService.getByPrice(price)
//    }

}