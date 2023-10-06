package dev.jombi.ubi.dto.request

data class RegisterRequest(
    val phone: String,
    val email: String,
    val password: String,
    val name: String
)
