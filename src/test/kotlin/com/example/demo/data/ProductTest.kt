package com.example.demo.data

import com.example.demo.exception.NotFoundException
import com.example.demo.repository.ProductRepository
import com.jayway.jsonpath.JsonPath
import jdk.incubator.http.HttpResponse
import net.minidev.json.JSONObject
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.lang.RuntimeException


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class ProductTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var productRepository: ProductRepository

    lateinit var httpHeaders: HttpHeaders

    @Before
    fun init() {
        httpHeaders = HttpHeaders()
        httpHeaders.add("Content-Type", "application/json")
    }

    @Test
    fun getProductText() {
        val product = Product(name = "Flutter", price = 1500)
        productRepository.insert(product)
        val id = productRepository.findByName(product.name).id
        mockMvc.perform(
                MockMvcRequestBuilders.get("/products/$id")
                        .headers(httpHeaders)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(product.name))
                .andExpect(jsonPath("$.price").value(product.price))

    }

    @Test
    fun createProductTest() {
        val request = JSONObject()
        request["name"] = "Harry Potter"
        request["price"] = "450"
        // 選擇 org.springframework.http.HttpHeaders
        val requestBuilder = MockMvcRequestBuilders.post("/products")
                .headers(httpHeaders)
                .content(request.toString())

        mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.name").value(request["name"].toString()))
                .andExpect(jsonPath("$.price").value(request["price"].toString().toInt()))
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Content-Type", "application/json"))
    }

    @Test
    fun updateProductTest() {

        val product = Product(name = "", price = 3000)
        productRepository.insert(product)
        val id = productRepository.findByName(product.name).id
        var request = JSONObject(
                mapOf(
                        "id" to id,
                        "name" to "iphone IOS",
                        "price" to 3500
                )
        )
        val requestBuilders = MockMvcRequestBuilders.put("/products/$id")
                .headers(httpHeaders)
                .content(request.toString())
        mockMvc.perform(requestBuilders)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id").value(request["id"].toString()))
                .andExpect(jsonPath("$.name").value(request["name"].toString()))
                .andExpect(jsonPath("$.price").value(request["price"].toString().toInt()))
    }

    @Test(expected = RuntimeException::class)
    fun deleteProductTest() {
        val product = Product(name = "Windows", price = 5000)
        productRepository.insert(product)
        val id = productRepository.findByName(product.name).id

        val requestBuilders = MockMvcRequestBuilders.delete("/products/$id")
                .headers(httpHeaders)

        mockMvc.perform(requestBuilders)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent)

        productRepository.findById(id)
                .orElseThrow(NotFoundException())
    }

    @After
    fun clear() {
        productRepository.deleteAll()
    }
}