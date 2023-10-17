package dev.jombi.ubi.handler

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import dev.jombi.ubi.util.response.*
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorHandler {
    private val LOGGER = LoggerFactory.getLogger(ErrorHandler::class.java)
    @ExceptionHandler(CustomError::class)
    protected fun handleCustomException(e: CustomError) =
        ResponseEntity.status(e.reason.status).body(GuidedResponseBuilder(e.reason, e.reason.message).noData())

    @ExceptionHandler(Exception::class)
    protected fun handleException(e: Exception): ResponseEntity<GuidedResponse<Any>> {
        LOGGER.error("Some error: ", e)
        return ResponseEntity.status(500).body(
            GuidedResponseBuilder(
                ErrorStatus.INTERNAL_SERVER_ERROR,
                "${e.message}", //  ${e.cause}
            ).noData()
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    protected fun handleException(e: HttpMessageNotReadableException): ResponseEntity<GuidedResponse<Any>> {
        return ResponseEntity.status(400).body(
            GuidedResponseBuilder(
                ErrorStatus.MISSING_PARAMETER,
                when (val cause = e.cause) {
                    is MissingKotlinParameterException -> {
                        "Missing parameter: ${cause.parameter.name}"
                    }
                    else -> {
                        LOGGER.warn(e.localizedMessage)
                        ErrorStatus.MISSING_PARAMETER.message
                    }
                }
            ).noData()
        )
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    protected fun handleException(e: HttpRequestMethodNotSupportedException) =
        ResponseEntity.status(405).body(
            GuidedResponseBuilder(
                ErrorStatus.INVALID_REQUEST_METHOD,
                "${e.method} is not allowed from this request. (${e.supportedMethods?.joinToString(", ")})"
            )
        )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleException(e: MethodArgumentNotValidException) =
        ResponseEntity.status(400).body(
            GuidedResponseBuilder(
                ErrorStatus.MISSING_PARAMETER,
                e.bindingResult.fieldError?.defaultMessage ?: "default"
            ).noData()
        )
}