package dev.jombi.ubi.dto.request

import java.util.UUID

data class AssembleRequest(
    val target: UUID,
    val message: String = "hi"
)
