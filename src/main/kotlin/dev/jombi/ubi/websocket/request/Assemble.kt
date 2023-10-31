package dev.jombi.ubi.websocket.request

data class Assemble(
    val userId: String,
    val status: InviteStatus
)