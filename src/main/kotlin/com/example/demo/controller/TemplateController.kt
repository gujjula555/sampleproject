package com.example.demo.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class TemplateController {

    @RequestMapping("/temp")
    fun template(
            map : ModelMap): String {
        map.addAttribute("host", "https://www.youtube.com/")
        return "temp.html"
    }
}