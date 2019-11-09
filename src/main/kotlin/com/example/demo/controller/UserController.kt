package com.example.demo.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam


@Controller
class UserController {


    @RequestMapping("/MyFirstPage")
    fun greeting(@RequestParam(value = "title", required = false, defaultValue = "Emilia")
                 title: String
                 , model: Model): String {
        model.addAttribute("name", title)
        return "index"
    }
}