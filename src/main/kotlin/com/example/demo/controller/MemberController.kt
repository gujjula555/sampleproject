package com.example.demo.controller

import com.example.demo.entity.Member
import com.example.demo.entity.MemberRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@RestController
@RequestMapping(("/member"))
class MemberController {


    @GetMapping
    fun get(httpServletRequest: HttpServletRequest): String {
        val cookies = httpServletRequest.cookies
        if (cookies.isNotEmpty()) {
            cookies.forEachIndexed { index, cookie ->
                if (cookie.name == "firstName" && cookie.value.isNotEmpty()) {
                    return "歡迎回來，${cookie.value}"
                }
            }
        }
        return "Oops"
    }

    @RequestMapping(method = [RequestMethod.POST])
    fun login(
            httpServletRequest: HttpServletRequest,
            httpServletResponse: HttpServletResponse,
            @Valid
            @ModelAttribute
            memberRequest: MemberRequest
    ): String {
        val cookies = httpServletRequest.cookies
        cookies.forEachIndexed { index, cookie ->
            if (cookie.name == "firstName") {
                cookies[index] == null
                return@forEachIndexed
            }
        }
        println("${memberRequest.firstName} / ${memberRequest.password}")
        val cookie = Cookie("firstName", memberRequest.firstName)
        val member = Member(memberRequest.firstName!!, memberRequest.password!!)
        httpServletResponse.addCookie(cookie)
        return "歡迎回來，${member.account}"
    }

    @RequestMapping(method = [RequestMethod.DELETE])
    fun deleteCookie(httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse) {
//        val cookies = httpServletRequest.cookies
//        println(cookies)
//        cookies.forEachIndexed { index, cookie ->
//            if (cookie.name == "firstName") {
//                cookies[index] == null
//            }
//        }

    }


}