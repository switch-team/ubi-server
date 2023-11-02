package dev.jombi.ubi.util.response

import org.springframework.http.HttpStatus

enum class ErrorStatus(val message: String, override val statusCode: Int, val status: HttpStatus = HttpStatus.OK) : Status {
    INCORRECT_PASSWORD("Incorrect password.", 101, HttpStatus.FORBIDDEN),
    EMAIL_ALREADY_EXISTS("Email is already registered.", 102),
    PHONE_ALREADY_EXISTS("Phone is already registered.", 102), // 102: auth conflict

    USER_NOT_FOUND("User not found.", 201, HttpStatus.UNAUTHORIZED),
    USER_ALREADY_EXISTS("User already exists.", 202),

    USER_IS_NOT_FRIEND("This user is not friend or requested.", 301),
    NO_SELF_CONFIRM("You can't confirm yourself.", 302, HttpStatus.BAD_REQUEST),
    USER_DO_NOT_HAVE_FRIEND("This user don't have any friends.", 303),
    FRIEND_REQUEST_ALREADY_SENT("Friend request already sent.", 304, HttpStatus.BAD_REQUEST),
    USER_IS_ALREADY_FRIEND("User is already a friend.", 305, HttpStatus.BAD_REQUEST),

    ARTICLE_NOT_FOUND("Article not found", 401, HttpStatus.NOT_FOUND),
    USER_IS_NOT_WRITER("This user is not writer", 402, HttpStatus.BAD_REQUEST),
    ALREADY_LIKED("You already liked this article.", 411, HttpStatus.BAD_REQUEST),
    CANT_LIKE_OWN_ARTICLE("You can't like your article.", 412, HttpStatus.BAD_REQUEST),


    USER_ALREADY_JOINED_ASSEMBLE("User is already in your assemble", 501),
    NO_ASSEMBLE_ROOM("No assemble rooms found.", 502),
    NOT_ASSEMBLE_MEMBER("You are not assemble member.", 503),
    ALREADY_ANSWERED("You already answered to this assemble.", 504),

    MALFORMED_TOKEN("Token is malformed.", 900, HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN("Token is expired.", 901, HttpStatus.UNAUTHORIZED),
    FILE_EXTENSION_NOT_PROVIDED("File extension is not provided.", 902, HttpStatus.BAD_REQUEST),
    MISSING_PARAMETER("Missing Parameter.", 903, HttpStatus.BAD_REQUEST),
    NOT_SUPPORTED_CONTENT_TYPE("Content type '%s' is not supported.", 903, HttpStatus.BAD_REQUEST),
    INVALID_REQUEST_METHOD("Invalid request method.", 904, HttpStatus.BAD_REQUEST),
    INVALID_PATH_VARIABLE("Invalid path variable.", 905, HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("An error has occurred.", 999, HttpStatus.INTERNAL_SERVER_ERROR),

}