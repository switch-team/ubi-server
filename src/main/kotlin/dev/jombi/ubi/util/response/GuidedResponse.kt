package dev.jombi.ubi.util.response

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus

data class GuidedResponse<T>(
    val status: HttpStatus,
    val message: String,
    @JsonInclude(JsonInclude.Include.NON_NULL) val data: T?
)

class GuidedResponseBuilder {
    var status: HttpStatus? = null
    var message: String? = null

    fun noData(): GuidedResponse<Any> {
        return GuidedResponse(status ?: HttpStatus.OK, message ?: "Success", null)
    }

    fun <T> build(data: T): GuidedResponse<T> {
        return GuidedResponse(status ?: HttpStatus.OK, message ?: "Success", data)
    }
}

fun GuidedResponseBuilder(status: HttpStatus, message: String) =
    GuidedResponseBuilder { this.status = status;this.message = message }

fun GuidedResponseBuilder(builder: GuidedResponseBuilder.() -> Unit): GuidedResponseBuilder =
    GuidedResponseBuilder().apply(builder)