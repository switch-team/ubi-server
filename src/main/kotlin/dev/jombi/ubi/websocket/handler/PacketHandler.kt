package dev.jombi.ubi.websocket.handler

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dev.jombi.ubi.websocket.BaseMessage
import dev.jombi.ubi.websocket.MessageType
import dev.jombi.ubi.websocket.request.Assemble
import dev.jombi.ubi.websocket.request.InviteStatus
import dev.jombi.ubi.websocket.request.CreateAssembleRequest
import dev.jombi.ubi.websocket.request.InviteAssembleRequest
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

private val MAPPER = jacksonObjectMapper()

@Component
class PacketHandler(val redis: StringRedisTemplate) {
    fun handleAssembleJoin(session: WebSocketSession, request: CreateAssembleRequest) {
        session.run {
            val p = principal!!.name
            if (redis.hasKey(p))
                return noData(MessageType.ASSEMBLE_ALREADY_EXISTS)
            val bound = redis.boundHashOps<String, String>(p)
            bound.put("title", request.title)
            return noData() // OK
        }
    }

    fun handleAssembleInvite(session: WebSocketSession, request: InviteAssembleRequest) {
        session.run {
            val p = principal!!.name
            if (!redis.hasKey(p))
                return noData(MessageType.NO_ASSEMBLE_ROOM)
            val n = redis.opsForHash<String, String>()
            if (n.hasKey(p, request.userId))
                return noData(MessageType.USER_ALREADY_JOINED)
            n.put(p, request.userId, InviteStatus.PENDING.name)

            return build(body = n.entries(p).filter { it.key != "title" }.map { Assemble(it.key, InviteStatus.valueOf(it.value)) })
        }
    }

    fun handleAssembleList(session: WebSocketSession) {
        session.run {
            val p = principal!!.name
            if (!redis.hasKey(p))
                return noData(MessageType.NO_ASSEMBLE_ROOM)
            val n = redis.opsForHash<String, String>()
            return build(body = n.entries(p).filter { it.key != "title" }.map { Assemble(it.key, InviteStatus.valueOf(it.value)) })
        }
    }
}

private fun WebSocketSession.noData(type: MessageType = MessageType.SUCCESS) =
        sendMessage(TextMessage(BaseMessage<Any>(type).toBody(MAPPER)))

private fun <T> WebSocketSession.build(type: MessageType = MessageType.SUCCESS, body: T) =
    sendMessage(TextMessage(BaseMessage(type, body).toBody(MAPPER)))