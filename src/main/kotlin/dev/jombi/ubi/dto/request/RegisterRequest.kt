package dev.jombi.ubi.dto.request

data class RegisterRequest(
    val number: String,
    val email: String,
    val password: String,
    val name: String
)
