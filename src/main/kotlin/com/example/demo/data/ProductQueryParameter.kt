package com.example.demo.data

data class ProductQueryParameter(
        var keyword: String = "",
        var orderBy: String = "id",
        var sortRule: String = ""
)