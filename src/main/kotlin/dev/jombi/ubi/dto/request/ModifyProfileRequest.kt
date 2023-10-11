package dev.jombi.ubi.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class ModifyProfileRequest(
    @field:Size(min = 2, max = 10)
    val name: String? = null,
    val phone: String? = null,
    @field:Email
    val email: String? = null
)