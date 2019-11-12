package com.example.demo.data

import jdk.incubator.http.HttpResponse
import net.minidev.json.JSONObject
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class ProductTest {

    @Autowired
    lateinit var mockMvc : MockMvc

    @Test
    fun createProductTest(){
        val request = JSONObject()
        request["name"] = "Harry Potter"
        request["price"] = "450"
        // 選擇 org.springframework.http.HttpHeaders
        val httpHeaders = HttpHeaders()
        httpHeaders.add("Content-Type", "application/json")

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

}