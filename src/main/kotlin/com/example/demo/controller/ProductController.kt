package com.example.demo.controller

import com.example.demo.data.Product
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.*
import java.util.stream.Collectors
import javax.websocket.server.PathParam

@RestController
@RequestMapping("/products")
class ProductController {


    val productDB = arrayListOf(
            Product("B001", "Emilia"),
            Product("B002", "Andy")
    )

    @GetMapping("/{id}")
    fun getProduct(@PathVariable("id") id: String): ResponseEntity<Product> {
        var productOp: Optional<Product> = productDB.stream()
                .filter {
                    it.id == id
                }.findFirst()
        if (productOp.isPresent) {
            val product = productOp.get()
            return ResponseEntity.ok(product)
        }
        return ResponseEntity.notFound().build()
    }


    @PostMapping
    fun createProduct(@RequestBody request: Product): ResponseEntity<Product> {
        var isIdDuplicate = productDB.stream().anyMatch {
            it.id == request.id
        }
        if (isIdDuplicate) {
            return ResponseEntity.unprocessableEntity().build()
        }
        val product = Product(request.id, request.name)
        productDB.add(product)

        val uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.id)
                .toUri()

        return ResponseEntity.created(uri).build()
    }

    @PutMapping("/{id}")
    fun updateProduct(
            @PathVariable("id") id: String,
            @RequestBody request: Product
    ): ResponseEntity<Product> {
        println("YES")
        val productOp = productDB.stream()
                .filter {
                    it.id == id
                }.findFirst()
        if (!productOp.isPresent) {
            return ResponseEntity.notFound().build()
        }
        val oldProduct = productOp.get()
        val product = Product()
        val index = productDB.indexOf(oldProduct)
        product.id = oldProduct.id
        product.name = request.name
        productDB[index] = product
        return ResponseEntity.ok(product)
    }


    @RequestMapping("/{id}", method = [RequestMethod.DELETE])
    fun deleteProduct(
            @PathVariable("id") id: String
    ): ResponseEntity<Product> {

        val productOp = productDB.stream()
                .filter {
                    it.id == id
                }.findFirst()

        if (productOp.isPresent) {
            productDB.remove(productOp.get())
        }

        return ResponseEntity.noContent().build()
    }

    @RequestMapping(method = [RequestMethod.GET])
    fun getProductByKeyword(
            @RequestParam(value = "keyword")
            keyword : String
    ):ResponseEntity<List<Product>>{
        val products = productDB.stream()
                .filter {
                    it.id.contains(keyword)
                }.collect(Collectors.toList())
        return ResponseEntity.ok(products)
    }
}