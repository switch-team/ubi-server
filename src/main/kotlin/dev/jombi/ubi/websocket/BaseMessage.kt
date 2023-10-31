package dev.jombi.ubi.websocket

import com.fasterxml.jackson.databind.ObjectMapper

data class BaseMessage<T>(
    val type: MessageType,
    val data: T? = null
) {
    fun toBody(mapper: ObjectMapper): String {
        return mapper.writeValueAsString(this)
    }
}