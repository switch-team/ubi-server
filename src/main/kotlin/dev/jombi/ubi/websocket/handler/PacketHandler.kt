package dev.jombi.ubi.websocket.handler

import com.fasterxml.jackson.databind.ObjectMapper
import dev.jombi.ubi.dto.response.ViewArticleResponse
import dev.jombi.ubi.entity.Article
import dev.jombi.ubi.entity.Assemble
import dev.jombi.ubi.entity.Location
import dev.jombi.ubi.entity.User
import dev.jombi.ubi.repository.mongo.LocationRepository
import dev.jombi.ubi.service.AssembleService
import dev.jombi.ubi.service.FriendService
import dev.jombi.ubi.util.UUIDSafe
import dev.jombi.ubi.websocket.BaseMessage
import dev.jombi.ubi.websocket.InMemoryWebSocketStorage
import dev.jombi.ubi.websocket.MessageType
import dev.jombi.ubi.websocket.request.SendLocationWSRequest
import dev.jombi.ubi.websocket.response.NewFriendRequestWSResponse
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.net.URL
import java.util.UUID

@Component
class PacketHandler(
    private val loc: LocationRepository,
    private val assembleService: AssembleService,
    private val friendService: FriendService,
    private val mapper: ObjectMapper
) {
    fun handleSendLocation(session: WebSocketSession, req: SendLocationWSRequest) {
        val id = UUID.fromString(session.principal!!.name)
        val location = loc.save(Location(id, req.latitude, req.longitude))
        handleUpdatedLocation(id, location)
    }

    fun handleUpdatedLocation(idWhoChanged: UUID, location: Location) {
        InMemoryWebSocketStorage.broadcast(
            relatedAssembleUsers(idWhoChanged), build(
                MessageType.LOCATION_INFO, location
            )
        )
    }

    fun handleAssembleInvite(target: UUID, assemble: Assemble) {
        InMemoryWebSocketStorage.broadcast(listOf(target), build(MessageType.NEW_INVITE_REQUEST, assemble))
    }

    fun handleFriendRequest(idWhoSent: UUID, idWhoReceived: UUID) {
        InMemoryWebSocketStorage.broadcast(
            listOf(idWhoReceived), build(
                MessageType.NEW_FRIEND_REQUEST, NewFriendRequestWSResponse(idWhoSent)
            )
        )
    }

    fun handleNewArticle(userWhoSent: User, article: Article) { // send
        val friends = friendService.getFriendList(userWhoSent).user.map { UUID.fromString(it.id) }
        InMemoryWebSocketStorage.broadcast(
            friends,
            build(
                MessageType.NEW_POSTS,
                ViewArticleResponse(
                    id = article.id,
                    date = article.date,
                    title = article.title,
                    content = article.content,
                    likeCount = article.likedUser.size,
                    viewCount = article.viewCount,
                    writer = article.writer.name,
                    thumbnailImage = article.thumbnailImage?.url?.let { URL(it) }
                )
            )
        )
    }

    fun handleJoin(session: WebSocketSession) {
        val id = UUID.fromString(session.principal!!.name)
        relatedAssembleUsers(id).mapNotNull { loc.findById(it).orElse(null) }.forEach {
            session.sendMessage(build(MessageType.LOCATION_INFO, it))
        }
    }

    fun relatedAssembleUsers(id: UUID): List<UUID> {
        val asm = assembleService.getRelatedAssembles(id)
        val n = asm.map { it.users.map { it.user } + it.host }.flatten().distinct().toMutableList()
        n.removeIf { it == id }
        return n
    }

//    private fun noData(type: MessageType) = TextMessage(BaseMessage<Any>(type).toBody(mapper))

    private fun <T> build(type: MessageType, body: T) = TextMessage(BaseMessage(type, body).toBody(mapper))
}