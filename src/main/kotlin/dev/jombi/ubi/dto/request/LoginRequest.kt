package dev.jombi.ubi.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

data class LoginRequest(
    @field:NotNull
    @field:NotBlank
    val id: String,
    @field:NotNull
    @field:NotBlank
    @field:Pattern(regexp = "[a-zA-Z0-9!@#$%^&*()?~/]{8,32}")
    val password: String
)
