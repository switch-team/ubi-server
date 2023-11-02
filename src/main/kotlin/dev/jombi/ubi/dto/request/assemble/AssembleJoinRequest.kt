package dev.jombi.ubi.dto.request.assemble

import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.UUID

data class AssembleJoinRequest(
    val accept: Boolean
)
