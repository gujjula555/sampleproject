package com.example.demo.service

import com.example.demo.data.Product
import com.example.demo.data.ProductQueryParameter
import com.example.demo.database.ProductDataBase.Companion.productDB
import com.example.demo.exception.NotFoundException
import com.example.demo.exception.UnProcessableException
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.stream.Collectors


@Service
class ProductService {

    fun getProduct(id: String): Product {
        return productDB.stream()
                .filter {
                    it.id == id
                }.findFirst()
                .orElseThrow(NotFoundException("Product Not Found"))
    }

    fun createProduct(request: Product): Product {
        var isIdDuplicate = productDB.stream().anyMatch {
            it.id == request.id
        }
        if (isIdDuplicate) {
            throw UnProcessableException("Id is Duplicate")
        }
        val product = Product(request.id, request.name, request.price)
        productDB.add(product)
        return product
    }

    fun updateProduct(id: String, request: Product): Product {
        val oldProduct = getProduct(id)
        val product = Product()
        val index = productDB.indexOf(oldProduct)
        product.id = oldProduct.id
        product.name = request.name
        product.price = request.price
        productDB[index] = product
        return product
    }

    fun deleteProduct(id: String) {
        val deleteProduct = getProduct(id)
        productDB.remove(deleteProduct)
    }

    fun getProducts(productQueryParameter: ProductQueryParameter): List<Product> {
        var steam = productDB.stream()
                .filter {
                    it.id.contains(productQueryParameter.keyword)
                }
        if (productQueryParameter.orderBy == "price") {
            var comparator: Comparator<Product> = Comparator(
                    function = fun(p1: Product, p2: Product): Int {
                        return p1.price.compareTo(p2.price)
                    }
            )
        }
        val products = steam.collect(Collectors.toList())
        if (products.isEmpty()) {
            throw NotFoundException("There is no product id contains ${productQueryParameter.keyword}")
        }
        if (productQueryParameter.sortRule != "asc") {
            products.reverse()
        }
        return products
    }
}