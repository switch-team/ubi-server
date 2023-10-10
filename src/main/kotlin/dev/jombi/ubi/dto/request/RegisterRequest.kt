package dev.jombi.ubi.dto.request

import jakarta.validation.constraints.*
import org.springframework.validation.annotation.Validated

@Validated
data class RegisterRequest(
    @field:NotBlank
    val phone: String,
    @field:NotBlank
    @field:Email
    val email: String,
    @field:NotBlank
    @field:Pattern(regexp = "[a-zA-Z0-9!@#$%^&*()?~/]{8,32}")
    val password: String,
    @field:NotBlank
    @field:Size(min = 2, max = 16)
    val name: String,
)
