package dev.jombi.ubi.dto.response

import java.util.UUID

data class FriendDetailResponse(
    val id: UUID,
    val name: String,
    val phone: String,
    val email: String
)