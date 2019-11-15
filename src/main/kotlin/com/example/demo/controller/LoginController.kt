package com.example.demo.controller

import com.example.demo.entity.MemberRequest
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import javax.validation.Valid


@Controller
@RequestMapping("/login")
class LoginController {

    @GetMapping
    fun get(): String {
        return "login.html"
    }

}