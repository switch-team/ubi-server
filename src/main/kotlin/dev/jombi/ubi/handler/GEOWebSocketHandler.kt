package dev.jombi.ubi.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dev.jombi.ubi.websocket.BaseMessage
import dev.jombi.ubi.websocket.InMemoryWebSocketStorage
import dev.jombi.ubi.websocket.MessageType
import dev.jombi.ubi.websocket.handler.PacketHandler
import org.slf4j.LoggerFactory
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession

class GEOWebSocketHandler(val handler: PacketHandler) : WebSocketHandler {
    private val LOGGER = LoggerFactory.getLogger(GEOWebSocketHandler::class.java)
    private val MAPPER = jacksonObjectMapper()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        InMemoryWebSocketStorage.add(session)
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        if (message !is TextMessage) return
        val n = MAPPER.readValue(message.payload, BaseMessage::class.java)
        if (n.data != null && n.data !is Map<*, *>)
            session.close(CloseStatus.BAD_DATA)
        if (n.type.isServerSide)
            session.close(CloseStatus.BAD_DATA) // request must send client side type
        val strData = n.data?.let{ MAPPER.writeValueAsString(it) }
        try {
            when (n.type) {
                MessageType.HOST_ASSEMBLE -> handler.handleAssembleJoin(session, MAPPER.readValue(strData ?: return))
                MessageType.INVITE_ASSEMBLE -> handler.handleAssembleInvite(session, MAPPER.readValue(strData ?: return))
                MessageType.LIST_ASSEMBLE -> handler.handleAssembleList(session)
                else -> return session.close(CloseStatus.NOT_ACCEPTABLE)
            }
        } catch (e: MissingKotlinParameterException) {
            // BadRequest handle
        }
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        LOGGER.error(session.id, exception)
    }

    override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
        InMemoryWebSocketStorage.remove(session)
    }

    override fun supportsPartialMessages() = false
}

private inline fun <reified T> ObjectMapper.readValue(strData: String) = readValue(strData, T::class.java)
