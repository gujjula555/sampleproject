package com.example.demo.database

import com.example.demo.data.Product

class ProductDataBase {

    companion object{
        public val productDB = arrayListOf(
                Product("B001", "Kotlin", 3000),
                Product("B002", "Java Android", 4000),
                Product("B003", "Kotlin Android", 5000),
                Product("B004", "Flutter", 4500),
                Product("B005", "Java", 2000)
        )
    }

}