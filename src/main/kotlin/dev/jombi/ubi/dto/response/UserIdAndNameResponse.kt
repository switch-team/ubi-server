package dev.jombi.ubi.dto.response

data class UserIdAndNameResponse(
    val id: String,
    val name: String
)

data class PendingResponse(val sender: UserIdAndNameResponse/*, val receiver: UserIdAndNameResponse*/)
