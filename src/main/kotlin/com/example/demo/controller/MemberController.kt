package com.example.demo.controller

import com.example.demo.entity.Member
import com.example.demo.entity.MemberRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(("/member"))
class MemberController {


    @RequestMapping(method = [RequestMethod.GET])
    fun login(
            @ModelAttribute
            memberRequest: MemberRequest
    ) : String {
        println("${memberRequest.account} / ${memberRequest.password}")
        val member = Member(memberRequest.account!!, memberRequest.password!!)
        return  "歡迎回來，${member.account}"
    }
}