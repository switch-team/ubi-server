package dev.jombi.ubi.config

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import dev.jombi.ubi.handler.GEOWebSocketHandler
import dev.jombi.ubi.websocket.handler.PacketHandler
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer


@Configuration
@EnableWebSocket
class WebSocketConfig(private val handler: PacketHandler) : WebSocketConfigurer, WebSocketMessageBrokerConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(myHandler(), "/ws")
    }

    @Bean
    fun myHandler(): WebSocketHandler {
        return GEOWebSocketHandler(handler)
    }

}