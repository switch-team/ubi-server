package dev.jombi.ubi.websocket.request

import dev.jombi.ubi.util.state.InviteStatus

data class Assemble(
    val userId: String,
    val message: String,
    val status: InviteStatus
)