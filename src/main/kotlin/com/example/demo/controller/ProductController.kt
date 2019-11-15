package com.example.demo.controller

import ProductRequest
import com.example.demo.entity.Product
import com.example.demo.entity.ProductQueryParameter
import com.example.demo.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping("/products")
class ProductController {


    @Autowired
    lateinit var productService: ProductService

    @GetMapping("/{id}")
    fun getProduct(@PathVariable("id") id: String): ResponseEntity<Product> {
        val product = productService.getProduct(id)
        return ResponseEntity.ok(product)
    }


    @PostMapping
    fun createProduct(
            @RequestBody
            @Valid
            request: ProductRequest
    ): ResponseEntity<Product> {

        val product = productService.createProduct(request)

        val uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.id)
                .toUri()

        return ResponseEntity.created(uri).body(product)
    }

    @PutMapping("/{id}")
    fun updateProduct(
            @PathVariable("id") id: String,
            @Valid
            @RequestBody request: ProductRequest
    ): ResponseEntity<Product> {
        val product = productService.updateProduct(id, request)
        return ResponseEntity.ok(product)
    }


    @RequestMapping("/{id}", method = [RequestMethod.DELETE])
    fun deleteProduct(
            @PathVariable("id") id: String
    ): ResponseEntity<Product> {
        productService.deleteProduct(id)
        return ResponseEntity.noContent().build()
    }

    @RequestMapping(method = [RequestMethod.GET])
    fun getProductByKeyword(
            @ModelAttribute productQueryParameter: ProductQueryParameter
    ): ResponseEntity<List<Product>> {
        val products = productService.getProducts(productQueryParameter)
        return ResponseEntity.ok(products)
    }
}