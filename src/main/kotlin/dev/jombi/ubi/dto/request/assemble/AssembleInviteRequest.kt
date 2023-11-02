package dev.jombi.ubi.dto.request.assemble

import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.UUID

data class AssembleInviteRequest(
    @field:NotBlank(message = "Must not be blank: id")
    @field:UUID(message = "id must be uuid format")
    val id: String,
    val message: String?
)