package dev.jombi.ubi.websocket.request

data class Assemble(
    val userId: String,
    val message: String,
    val status: InviteStatus
)