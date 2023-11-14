package dev.jombi.ubi.auth.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

data class LoginRequest(
    @field:NotNull
    @field:NotBlank(message = "Must not be blank: id")
    val id: String,
    @field:NotNull
    @field:NotBlank(message = "Must not be blank: password")
    @field:Pattern(regexp = "[a-zA-Z0-9!@#$%^&*()?~/]{8,32}")
    val password: String
)
