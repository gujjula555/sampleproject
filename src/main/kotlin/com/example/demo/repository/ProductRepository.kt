package com.example.demo.repository

import com.example.demo.data.Product
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.repository.MongoRepository

interface ProductRepository : MongoRepository<Product, String> {


    fun findByNameLike(name: String): List<Product>


    fun findByNameLike(name: String, sort: Sort): List<Product>

}