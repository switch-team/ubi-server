package dev.jombi.ubi.util.response

import com.fasterxml.jackson.annotation.JsonInclude

data class GuidedResponse<T>(
    val status: Int,
    val message: String,
    @JsonInclude(JsonInclude.Include.NON_NULL) val data: T?
)

class GuidedResponseBuilder {
    var status: Status? = null
    var message: String? = null

    fun noData(): GuidedResponse<Any> {
        return GuidedResponse(status?.statusCode ?: 0, message ?: "Success", null)
    }

    fun <T> build(data: T): GuidedResponse<T> {
        return GuidedResponse(status?.statusCode ?: 0, message ?: "Success", data)
    }
}

fun GuidedResponseBuilder(status: Status, message: String) =
    GuidedResponseBuilder {
        this.status = status
        this.message = message
    }

fun GuidedResponseBuilder(builder: GuidedResponseBuilder.() -> Unit): GuidedResponseBuilder =
    GuidedResponseBuilder().apply(builder)