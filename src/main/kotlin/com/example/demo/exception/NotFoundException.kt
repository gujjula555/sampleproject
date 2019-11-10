package com.example.demo.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.function.Supplier

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class NotFoundException(message: String = "") : RuntimeException(message), Supplier<NotFoundException> {
    override fun get(): NotFoundException = NotFoundException(message!!)

}
