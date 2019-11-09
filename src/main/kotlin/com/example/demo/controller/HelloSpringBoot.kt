package com.example.demo.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloSpringBoot {
    //透過 @RestController 指定從/會被對應到此hello()方法
    @RequestMapping("/")
    fun sayHello() : String{
        return "向全世界說聲Spring Boot 很高興認識你!"
    }
}