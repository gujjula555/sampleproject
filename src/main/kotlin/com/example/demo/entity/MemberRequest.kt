package com.example.demo.entity

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class MemberRequest {

    @NotNull
    var account: String? = null
    @NotNull
    @Size(min = 6, message = "Password need to be greater than 6 character")
    var password: String? = null
}