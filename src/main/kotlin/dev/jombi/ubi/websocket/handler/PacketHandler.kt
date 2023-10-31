package dev.jombi.ubi.websocket.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.client.MongoClient
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import dev.jombi.ubi.websocket.BaseMessage
import dev.jombi.ubi.websocket.MessageType
import dev.jombi.ubi.websocket.request.Assemble
import dev.jombi.ubi.websocket.request.InviteStatus
import dev.jombi.ubi.websocket.request.CreateAssembleRequest
import dev.jombi.ubi.websocket.request.InviteAssembleRequest
import dev.jombi.ubi.websocket.structure.AssembleStruct
import org.bson.Document
import org.springframework.data.mongodb.core.query.BasicQuery
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

@Component
class PacketHandler(private val mongo: MongoClient, private val mapper: ObjectMapper) {
    fun handleAssembleJoin(session: WebSocketSession, request: CreateAssembleRequest) {
        session.run {
            val p = principal!!.name
            val assembleTable = mongo.getDatabase("assemble")
            if (assembleTable.listCollectionNames().contains(p)) // any -> kotlin 기본 리스트 함수 (조건이 만족하는게 하나라도 있으면 true)
                return noData(MessageType.ASSEMBLE_ALREADY_EXISTS)
            assembleTable.createCollection(p)
            assembleTable.getCollection(p)
                .insertOne(Document.parse(mapper.writeValueAsString(AssembleStruct(request.title))))
            return noData() // OK
        }
    }
    fun handleAssembleRemove(session: WebSocketSession) {
        session.run {
            val p = principal!!.name
            val assembleTable = mongo.getDatabase("assemble")

            if (assembleTable.listCollectionNames().contains(p))
                assembleTable.getCollection(p).drop()
        }
    }

    fun handleAssembleInvite(session: WebSocketSession, request: InviteAssembleRequest) {
        session.run {
            val p = principal!!.name
            val assembleTable = mongo.getDatabase("assemble")
            if (!assembleTable.listCollectionNames().contains(p))
                return noData(MessageType.NO_ASSEMBLE_ROOM)
            val collection = assembleTable.getCollection(p)
            val document = Document.parse(mapper.writeValueAsString(Assemble(request.userId, InviteStatus.PENDING)))
            if (collection.find().first()!!.getList("users", Document::class.java).any { it["userId"].toString() == request.userId })
                return noData(MessageType.USER_ALREADY_JOINED)
            val result = collection.updateOne(Filters.exists("users"), Updates.push("users", document))
            if (result.modifiedCount > 0) {
                return build(body = collection.find().first()!!.let { AssembleStruct(it["title"].toString(), it.getList("users", Document::class.java).map { mapper.readValue(it.toJson(), Assemble::class.java) }) })
            }
        }
    }

    fun handleAssembleList(session: WebSocketSession) {
        session.run {
            val p = principal!!.name
            val assembleTable = mongo.getDatabase("assemble")
            if (!assembleTable.listCollectionNames().contains(p))
                return noData(MessageType.NO_ASSEMBLE_ROOM)
            return build(body = assembleTable.getCollection(p).find().first()!!.let { AssembleStruct(it["title"].toString(), it.getList("users", Document::class.java).map { mapper.readValue(it.toJson(), Assemble::class.java) }) })
        }
    }

    private fun WebSocketSession.noData(type: MessageType = MessageType.SUCCESS) =
        sendMessage(TextMessage(BaseMessage<Any>(type).toBody(mapper)))

    private fun <T> WebSocketSession.build(type: MessageType = MessageType.SUCCESS, body: T) =
        sendMessage(TextMessage(BaseMessage(type, body).toBody(mapper)))
}