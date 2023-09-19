package dev.jombi.ubi.util.response

import org.springframework.http.HttpStatus

enum class ErrorDetail(val message: String, val status: HttpStatus) {
    USER_NOT_FOUND("User not found.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("An error has occurred.", HttpStatus.INTERNAL_SERVER_ERROR)
}