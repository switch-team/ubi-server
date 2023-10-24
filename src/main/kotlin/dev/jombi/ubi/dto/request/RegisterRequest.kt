package dev.jombi.ubi.dto.request

import jakarta.validation.constraints.*
import org.springframework.validation.annotation.Validated

@Validated
data class RegisterRequest(
    @field:NotBlank(message = "Must not be blank: phone")
    @field:Pattern(regexp = "[-0-9]{10,13}")
    val phone: String,
    @field:NotBlank(message = "Must not be blank: email")
    @field:Email(message = "email is must be email format")
    val email: String,
    @field:NotBlank(message = "Must not be blank: password")
    @field:Pattern(regexp = "[a-zA-Z0-9!@#$%^&*()?~/]{8,32}", message = "password should have only `a-zA-Z!@#$%^&*()?~/`")
    val password: String,
    @field:NotBlank(message = "Must not be blank: name")
    @field:Size(min = 2, max = 16)
    val name: String,
)
