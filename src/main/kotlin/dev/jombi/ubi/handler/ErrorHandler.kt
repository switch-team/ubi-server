package dev.jombi.ubi.handler

import dev.jombi.ubi.util.response.CustomError
import dev.jombi.ubi.util.response.ErrorDetail
import dev.jombi.ubi.util.response.GuidedResponse
import dev.jombi.ubi.util.response.GuidedResponseBuilder
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorHandler {
    private val LOGGER = LoggerFactory.getLogger(ErrorHandler::class.java)
    @ExceptionHandler(CustomError::class)
    protected fun handleCustomException(e: CustomError) =
        ResponseEntity.status(e.reason.status).body(GuidedResponseBuilder(e.reason.status, e.reason.message).noData())

    @ExceptionHandler(Exception::class)
    protected fun handleException(e: Exception): ResponseEntity<GuidedResponse<Any>> {
        LOGGER.error("Some error: ", e)
        return ResponseEntity.status(500).body(
            GuidedResponseBuilder(
                ErrorDetail.INTERNAL_SERVER_ERROR.status,
                "${e.message}", //  ${e.cause}
            ).noData()
        )
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    protected fun handleException(e: HttpRequestMethodNotSupportedException) =
        ResponseEntity.status(405).body(
            GuidedResponseBuilder(
                HttpStatus.METHOD_NOT_ALLOWED,
                "${e.method} is not allowed from this request. (${e.supportedMethods?.joinToString(", ")})"
            )
        )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleException(e: MethodArgumentNotValidException) =
        ResponseEntity.status(400).body(
            GuidedResponseBuilder(
                HttpStatus.BAD_REQUEST,
                e.bindingResult.fieldError?.defaultMessage ?: "default"
            ).noData()
        )
}