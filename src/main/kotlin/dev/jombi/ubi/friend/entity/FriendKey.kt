package dev.jombi.ubi.friend.entity

import jakarta.persistence.Embeddable
import java.io.Serializable
import java.util.UUID

@Embeddable
data class FriendKey(
    val id: UUID = UUID.randomUUID(),
    val sender: UUID,
    val receiver: UUID
) : Serializable