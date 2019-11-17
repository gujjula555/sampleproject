package com.example.demo.entity

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class MemberRequest {

    @NotNull
    var lastName: String? = null

    @NotNull
    var firstName: String? = null

    @NotNull
    var telephoneOrEmail: String? = null

    @NotNull
    var birthday_year: Int = 1993

    @NotNull
    var birthday_month: Int = 9

    @NotNull
    var birthday_day: Int = 29

    @NotNull
    var sex: Int? = null
    @NotNull
    @Size(min = 6, message = "Password need to be greater than 6 character")
    var password: String? = null
}