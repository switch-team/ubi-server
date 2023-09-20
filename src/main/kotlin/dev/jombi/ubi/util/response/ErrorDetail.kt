package dev.jombi.ubi.util.response

import org.springframework.http.HttpStatus

enum class ErrorDetail(val message: String, val status: HttpStatus) {
    USER_NOT_FOUND("User not found.", HttpStatus.BAD_REQUEST),
    INCORRECT_PASSWORD("Incorrect password.", HttpStatus.BAD_REQUEST),
    MALFORMED_TOKEN("Token is malformed.", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN("Token is expired.", HttpStatus.UNAUTHORIZED),
    USER_ALREADY_EXISTS("User already exists.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("An error has occurred.", HttpStatus.INTERNAL_SERVER_ERROR);

}