package com.example.demo.entity

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "product")
class Product(
        var id: String = "",
        var name: String,
        var price: Int
) {

    constructor() : this("", "", 0)


}