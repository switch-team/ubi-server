package dev.jombi.ubi.controller

import dev.jombi.ubi.dto.request.LoginRequest
import dev.jombi.ubi.dto.request.RegisterRequest
import dev.jombi.ubi.dto.response.TokenResponse
import dev.jombi.ubi.service.AuthService
import dev.jombi.ubi.util.response.GuidedResponse
import dev.jombi.ubi.util.response.GuidedResponseBuilder
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(private val service: AuthService) {
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<GuidedResponse<TokenResponse>> {
        val token = service.authenticate(request.id, request.password)
        return ResponseEntity.ok(GuidedResponseBuilder { message = "Authenticated." }.build(TokenResponse(token)))
    }

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<GuidedResponse<Any>> {
        service.registerNew(
            name = request.name,
            email = request.email,
            phoneWithDash = request.phone,
            password = request.password
        )
        return ResponseEntity.ok(GuidedResponseBuilder { message = "Created." }.noData())
    }
}