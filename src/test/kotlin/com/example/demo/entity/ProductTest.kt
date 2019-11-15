package com.example.demo.entity

import com.example.demo.exception.NotFoundException
import com.example.demo.repository.ProductRepository
import net.minidev.json.JSONObject
import org.hamcrest.Matchers.hasSize
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
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
    fun getProductTest() {
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

        val result = mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.name").value(request["name"].toString()))
                .andExpect(jsonPath("$.price").value(request["price"].toString().toInt()))
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Content-Type", "application/json"))
                .andReturn()

        val response = result.response
        val resBody = org.json.JSONObject(response.contentAsString)
        val productId = resBody.getString("id")
        Assert.assertNotNull(productId.isNotEmpty())
        Assert.assertEquals(productId, productRepository.findByName(request["name"].toString()).id)
        Assert.assertEquals(resBody.getString("name"), request["name"].toString())
        Assert.assertEquals(resBody.getInt("price"), request["price"].toString().toInt())

        Assert.assertEquals(HttpStatus.CREATED.value(), response.status)
        Assert.assertEquals("application/json",
                response.contentType);
        Assert.assertEquals(result.request.requestURL.toString() + "/" + productId,
                response.getHeader("Location"));
    }

    @Test
    fun createProductWithoutNameTest() {
        val jsonObject = JSONObject()
        jsonObject["price"] = 400

        val requestBuilders = MockMvcRequestBuilders.post("/products")
                .headers(httpHeaders)
                .content(jsonObject.toString())

        mockMvc.perform(requestBuilders)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest)
    }

    @Test
    fun updateProductWithNegativePrice() {
        val product = Product(name = "Python Flask", price = 2100)
        productRepository.insert(product)
        val productId = productRepository.findByName(product.name).id

        val jsonObject = JSONObject()
        jsonObject["price"] = -500

        val requestBuilders = MockMvcRequestBuilders.put("/products/$productId")
                .headers(httpHeaders)
                .content(jsonObject.toString())

        mockMvc.perform(requestBuilders)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest)
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

    @Test
    fun getProductsSortByPriceAscTest() {
        val p1 = Product(name = "Spring Course", price = 1500)
        val p2 = Product(name = "FireBase Course", price = 1000)
        val p3 = Product(name = "Google Cloud Platform Course", price = 3500)
        val p4 = Product(name = "Microsoft Azure Course", price = 2500)
        val p5 = Product(name = "DB2 Guide", price = 500)

        productRepository.insert(listOf(p1, p2, p3, p4, p5))

        val requestBuilders = MockMvcRequestBuilders.get("/products")
                .param("keyword", "Course")
                .param("orderBy", "price")
                .param("sortRule", "asc")

        mockMvc.perform(requestBuilders)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("$", hasSize<Int>(4)))
                .andExpect(jsonPath("$[0].id").value(productRepository.findByName(p2.name).id))
                .andExpect(jsonPath("$[1].id").value(productRepository.findByName(p1.name).id))
                .andExpect(jsonPath("$[2].id").value(productRepository.findByName(p4.name).id))
                .andExpect(jsonPath("$[3].id").value(productRepository.findByName(p3.name).id))
    }


    @After
    fun clear() {
        productRepository.deleteAll()
    }
}