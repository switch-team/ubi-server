package dev.jombi.ubi.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import dev.jombi.ubi.websocket.BaseMessage
import dev.jombi.ubi.websocket.InMemoryWebSocketStorage
import dev.jombi.ubi.websocket.MessageType
import dev.jombi.ubi.websocket.handler.PacketHandler
import org.slf4j.LoggerFactory
import org.springframework.web.socket.*

class GEOWebSocketHandler(private val handler: PacketHandler, private val mapper: ObjectMapper) : WebSocketHandler {
    private val LOGGER = LoggerFactory.getLogger(GEOWebSocketHandler::class.java)

    override fun afterConnectionEstablished(session: WebSocketSession) {
        InMemoryWebSocketStorage.add(session)
        handler.handleJoin(session)
        LOGGER.info("Joined: ${session.principal?.name}")
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        if (message !is TextMessage) return
        val n = mapper.readValue(message.payload, BaseMessage::class.java)
        if (n.data != null && n.data !is Map<*, *>)
            session.close(CloseStatus.BAD_DATA)
        if (n.type.isServerSide)
            session.close(CloseStatus.BAD_DATA) // request must send client side type
        val strData = n.data?.let{ mapper.writeValueAsString(it) }
        try {
            when (n.type) {
                MessageType.SEND_LOCATION -> handler.handleSendLocation(session, mapper.readValue(strData ?: return))
                else -> {} // does nothing
//                else -> return session.close(CloseStatus.NOT_ACCEPTABLE)
            }
        } catch (e: MissingKotlinParameterException) {
            // session.sendMessage(TextMessage(MAPPER.writeValueAsString(BaseMessage(MessageType.INVALID_VALUE, "Missing parameter: ${e.parameter.name}"))))
        } catch (e: InvalidFormatException) {
            //
        }
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        LOGGER.error(session.id, exception)
    }

    override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
        InMemoryWebSocketStorage.remove(session)
        LOGGER.info("Left: ${session.principal?.name}")
    }

    override fun supportsPartialMessages() = false
}

private inline fun <reified T> ObjectMapper.readValue(strData: String) = readValue(strData, T::class.java)
