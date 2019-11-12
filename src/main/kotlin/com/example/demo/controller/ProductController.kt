package com.example.demo.controller

import com.example.demo.data.Product
import com.example.demo.data.ProductQueryParameter
import com.example.demo.database.ProductDataBase.Companion.productDB
import com.example.demo.exception.NotFoundException
import com.example.demo.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.*
import java.util.function.Supplier
import java.util.stream.Collectors
import javax.websocket.server.PathParam
import kotlin.Comparator

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
    fun createProduct(@RequestBody request: Product): ResponseEntity<Product> {

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
            @RequestBody request: Product
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