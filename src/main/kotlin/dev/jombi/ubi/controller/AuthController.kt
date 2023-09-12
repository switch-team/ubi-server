package dev.jombi.ubi.controller

import dev.jombi.ubi.dto.request.LoginRequest
import dev.jombi.ubi.dto.request.RegisterRequest
import dev.jombi.ubi.service.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(service: AuthService, ) {
    @PostMapping("/login")
    fun login(request: LoginRequest) {

    }
    @PostMapping("/register")
    fun register(request: RegisterRequest) {

    }
}