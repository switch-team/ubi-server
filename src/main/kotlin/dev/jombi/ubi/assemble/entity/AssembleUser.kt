package dev.jombi.ubi.assemble.entity

import dev.jombi.ubi.assemble.entity.status.InviteStatus
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class AssembleUser(
    val user: UUID,
    val message: String,
    val status: InviteStatus,
)