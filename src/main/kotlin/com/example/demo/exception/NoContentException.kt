package com.example.demo.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NO_CONTENT)
class NoContentException(message: String = "") : RuntimeException(message)