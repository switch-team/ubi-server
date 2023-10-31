package dev.jombi.ubi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import dev.jombi.ubi.handler.GEOWebSocketHandler
import dev.jombi.ubi.websocket.handler.PacketHandler
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer


@Configuration
@EnableWebSocket
class WebSocketConfig(val handler: PacketHandler) : WebSocketConfigurer, WebSocketMessageBrokerConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(myHandler(), "/ws")
    }

    @Bean
    fun myHandler(): WebSocketHandler {
        return GEOWebSocketHandler(handler)
    }

}