package dev.jombi.ubi.websocket.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import dev.jombi.ubi.websocket.BaseMessage
import dev.jombi.ubi.websocket.MessageType
import dev.jombi.ubi.websocket.request.*
import dev.jombi.ubi.websocket.structure.AssembleStruct
import org.bson.Document
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import kotlin.system.exitProcess

@Component
class PacketHandler(private val mongo: MongoClient, private val mapper: ObjectMapper) {
    private fun wrap(data: Any): Document = Document.parse(mapper.writeValueAsString(data))
    private fun fetchAssembles(document: Document): List<Assemble> =
        document.getList("users", Document::class.java)
            .map { j -> mapper.readValue(j.toJson(), Assemble::class.java) }


    fun handleAssembleCreate(session: WebSocketSession, request: CreateAssembleRequest) {
        session.run {
            val p = principal!!.name
            val assembleTable = mongo.getDatabase("assemble")
            if (assembleTable.listCollectionNames().contains(p)) // any -> kotlin 기본 리스트 함수 (조건이 만족하는게 하나라도 있으면 true)
                assembleTable.getCollection(p).drop() // assemble reset

            assembleTable.createCollection(p)
            assembleTable.getCollection(p)
                .insertOne(wrap(AssembleStruct(request.title)))
            return build(body = checkAssemble(p))
        }
    }

    fun handleAssembleCheck(session: WebSocketSession, request: CheckAssembleRequest) {
        session.foreignAction(request.id) { _,_,_->true }
    }

    fun WebSocketSession.foreignAction(id: String, action: WebSocketSession.(MongoCollection<Document>, List<Assemble>, Int) -> Boolean) {
        val p = principal!!.name
        val assembleTable = mongo.getDatabase("assemble")
        if (!assembleTable.listCollectionNames().contains(id)) return noData(MessageType.NO_ASSEMBLE_ROOM)
        val collection = assembleTable.getCollection(id)
        val assembles = fetchAssembles(collection.find().first()!!)
        val user = assembles.indexOfFirst { it.userId == p }
        if (user < 0) return noData(MessageType.NOT_ASSEMBLE_PERSON)
        if (action(collection, assembles, user)) return build(body = checkAssemble(id))
    }

    fun handleAssembleJoin(session: WebSocketSession, request: JoinAssembleRequest) {
        session.foreignAction(request.id) { collection, assembles, user ->
            if (assembles[user].status != InviteStatus.PENDING) return@foreignAction noData(MessageType.ALREADY_ANSWERED).let { false }
            collection.updateOne(
                Filters.exists("users"), Updates.set(
                    "users.$user.status", (if (request.accept) InviteStatus.ACCEPTED else InviteStatus.REJECTED).name
                )
            )
            true
        }
    }

    fun handleAssembleInvite(session: WebSocketSession, request: InviteAssembleRequest) {
        try {
            session.run {
                val p = principal!!.name
                val assembleTable = mongo.getDatabase("assemble")
                if (!assembleTable.listCollectionNames().contains(p)) return noData(MessageType.NO_ASSEMBLE_ROOM)
                val collection = assembleTable.getCollection(p)
                val document = wrap(Assemble(request.userId, request.message, InviteStatus.PENDING))
                if (collection.find().first()!!.getList("users", Document::class.java)
                        .any { it["userId"].toString() == request.userId }
                ) return noData(MessageType.USER_ALREADY_JOINED)
                val result = collection.updateOne(Filters.exists("users"), Updates.push("users", document))
                if (result.modifiedCount > 0) {
                    return build(body = checkAssemble(p))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun handleAssembleList(session: WebSocketSession) {
        session.run {
            val p = principal!!.name
            checkAssemble(p)?.let { build(body = it) } ?: noData(MessageType.NO_ASSEMBLE_ROOM)
        }
    }

    private fun checkAssemble(id: String): AssembleStruct? {
        val assembleTable = mongo.getDatabase("assemble")
        if (!assembleTable.listCollectionNames().contains(id)) return null
        return assembleTable.getCollection(id).find().first()!!.let {
            AssembleStruct(it["title"].toString(), fetchAssembles(it))
        }
    }

    private fun WebSocketSession.noData(type: MessageType = MessageType.SUCCESS) =
        sendMessage(TextMessage(BaseMessage<Any>(type).toBody(mapper)))

    private fun <T> WebSocketSession.build(type: MessageType = MessageType.SUCCESS, body: T) =
        sendMessage(TextMessage(BaseMessage(type, body).toBody(mapper)))
}