package dev.jombi.ubi.friend.dto.request

import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.UUID

data class UserIdRequest(
    @field:NotBlank(message = "Must not be blank: id")
    @field:UUID(message = "id must be uuid format")
    val id: String
)
