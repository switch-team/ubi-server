package dev.jombi.ubi.util.response

import org.springframework.http.HttpStatus

enum class ErrorDetail(val message: String, val status: HttpStatus) {
    USER_NOT_FOUND("User not found.", HttpStatus.BAD_REQUEST),
    INCORRECT_PASSWORD("Incorrect password.", HttpStatus.BAD_REQUEST),
    MALFORMED_TOKEN("Token is malformed.", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN("Token is expired.", HttpStatus.UNAUTHORIZED),
    USER_ALREADY_EXISTS("User already exists.", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS("Email is already registered.", HttpStatus.BAD_REQUEST),
    PHONE_ALREADY_EXISTS("Phone is already registered.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("An error has occurred.", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_INVITED("This user is not invited", HttpStatus.BAD_REQUEST),
    NO_SELF_CONFIRM("You can't accept your request.", HttpStatus.BAD_REQUEST),
    USER_DO_NOT_HAVE_FRIEND("This user don't have any frineds.", HttpStatus.NOT_FOUND),
    ALREADY_SENT("Request already sent.", HttpStatus.BAD_REQUEST),
    USER_IS_ALREADY_FRIEND("User is already a friend.", HttpStatus.BAD_REQUEST),
    FILE_EXTENSION_NOT_PROVIDED("File extension is not provided.", HttpStatus.BAD_REQUEST),

}