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
    USER_DO_NOT_HAVE_FRIEND("This user don't have any friends.", HttpStatus.NOT_FOUND),
    FRIEND_REQUEST_ALREADY_SENT("Friend request already sent.", HttpStatus.BAD_REQUEST),
    ALREADY_LIKED("You already liked this article.", HttpStatus.BAD_REQUEST),
    CANT_LIKE_OWN_ARTICLE("You can't like your article.", HttpStatus.BAD_REQUEST),
    USER_IS_ALREADY_FRIEND("User is already a friend.", HttpStatus.BAD_REQUEST),
    FILE_EXTENSION_NOT_PROVIDED("File extension is not provided.", HttpStatus.BAD_REQUEST),
    ARTICLE_NOT_FOUND("Article not found", HttpStatus.NOT_FOUND),
    USER_IS_NOT_WRITER("This user is not writer", HttpStatus.BAD_REQUEST),
    USER_DO_NOT_HAVE_ARTICLE("This user had never written article before", HttpStatus.NOT_FOUND)

}