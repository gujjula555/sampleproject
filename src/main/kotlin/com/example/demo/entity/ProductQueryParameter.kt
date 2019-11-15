package com.example.demo.entity

data class ProductQueryParameter(
        var keyword: String = "",
        var orderBy: String = "id",
        var sortRule: String = ""
)