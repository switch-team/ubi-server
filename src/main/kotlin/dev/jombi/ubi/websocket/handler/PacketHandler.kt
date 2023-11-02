package dev.jombi.ubi.websocket.handler

import com.fasterxml.jackson.databind.ObjectMapper
import dev.jombi.ubi.entity.Location
import dev.jombi.ubi.repository.mongo.LocationRepository
import dev.jombi.ubi.service.AssembleService
import dev.jombi.ubi.websocket.BaseMessage
import dev.jombi.ubi.websocket.InMemoryWebSocketStorage
import dev.jombi.ubi.websocket.MessageType
import dev.jombi.ubi.websocket.request.SendLocationWSRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Component
class PacketHandler(
    private val loc: LocationRepository,
    private val assembleService: AssembleService,
    private val mapper: ObjectMapper
) {
    fun handleSendLocation(session: WebSocketSession, req: SendLocationWSRequest) {
        val id = UUID.fromString(session.principal!!.name)
        val location = loc.save(Location(id, req.latitude, req.longitude))

        InMemoryWebSocketStorage.broadcast(
            relatedUsers(id),
            build(
                MessageType.LOCATION_INFO,
                location
            )
        )
    }

    fun handleJoin(session: WebSocketSession) {
        val id = UUID.fromString(session.principal!!.name)
        relatedUsers(id).map { loc.findById(it) }.forEach {
            it.getOrNull()?.also {
                session.sendMessage(build(MessageType.LOCATION_INFO, it))
            }
        }
    }

    fun relatedUsers(id: UUID): List<UUID> {
        val asm = assembleService.getRelatedAssembles(id)
        val n = asm.map { it.users.map { it.user } + it.host }.flatten().distinct().toMutableList()
        n.removeIf { it == id }
        return n
    }

    private fun noData(type: MessageType) =
        TextMessage(BaseMessage<Any>(type).toBody(mapper))

    private fun <T> build(type: MessageType, body: T) =
        TextMessage(BaseMessage(type, body).toBody(mapper))
}