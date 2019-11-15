package com.example.demo.service

import ProductRequest
import com.example.demo.entity.Product
import com.example.demo.entity.ProductQueryParameter
import com.example.demo.exception.NotFoundException
import com.example.demo.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service


@Service
class ProductService {

    @Autowired
    lateinit var productRepository: ProductRepository

    fun getProduct(id: String): Product {

        return productRepository.findById(id)
                .orElseThrow(NotFoundException("Product Not Found"))
    }

    fun createProduct(request: ProductRequest): Product {
        val product = Product()
        product.name = request.name!!
        product.price = request.price!!
        productRepository.insert(product)
        return productRepository.findByName(product.name)
    }

    fun updateProduct(id: String, request: ProductRequest): Product {

        val oldProduct = getProduct(id)
        val product = Product()
        product.id = oldProduct.id
        product.name = request.name!!
        product.price = request.price!!
        return productRepository.save(product)
    }

    fun deleteProduct(id: String) {
        val deleteProduct = getProduct(id)
        productRepository.delete(deleteProduct)
    }

    fun getProducts(productQueryParameter: ProductQueryParameter): List<Product> {
        val keyword = productQueryParameter.keyword
        val orderBy = productQueryParameter.orderBy
        val sortRule = productQueryParameter.sortRule
        var sort: Sort? = null
        if (orderBy != "" && sortRule != "") {
            val direction =
                    if (sortRule == "asc")
                        Sort.Direction.ASC
                    else
                        Sort.Direction.DESC
            sort = Sort.by(direction, orderBy)
        }
        println(productRepository.findAll())
        return if (sort == null) {
            productRepository.findByNameLike(keyword)
        } else {
            productRepository.findByNameLike(keyword, sort)
        }
    }
}