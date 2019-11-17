package com.example.demo.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*


@Controller
@RequestMapping("/login")
class LoginController {

    @GetMapping
    fun get(): String {
        return "login.html"
    }


}