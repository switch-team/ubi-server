package dev.jombi.ubi.dto.request

import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.UUID

data class UserIdRequest(
    @field:NotBlank
    @field:UUID
    val id: String
)
