package dev.jombi.ubi.entity

import dev.jombi.ubi.util.state.InviteStatus
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document
data class AssembleUser(
    val user: UUID,
    val message: String,
    val approved: InviteStatus,
)