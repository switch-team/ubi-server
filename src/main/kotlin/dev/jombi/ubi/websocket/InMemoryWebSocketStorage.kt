package dev.jombi.ubi.websocket

import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.UUID

object InMemoryWebSocketStorage {
    private val sessions = ArrayList<WebSocketSession>()
    fun add(user: WebSocketSession) {
        synchronized(sessions) {
            sessions.add(user)
        }
    }
    fun remove(user: WebSocketSession) {
        synchronized(sessions) {
            sessions.remove(user)
        }
    }

    fun broadcast(message: TextMessage) {
        synchronized(sessions) {
            sessions.forEach {
                it.sendMessage(message)
            }
        }
    }

    fun broadcast(principals: List<UUID>, message: TextMessage) {
        synchronized(sessions) {
            sessions.filter { principals.contains(it.principal?.name?.let { UUID.fromString(it) }) }.forEach {
                it.sendMessage(message)
            }
        }
    }
}