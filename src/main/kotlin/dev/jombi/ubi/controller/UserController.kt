package dev.jombi.ubi.controller

import dev.jombi.ubi.dto.Profile
import dev.jombi.ubi.dto.response.UserIdAndNameResponse
import dev.jombi.ubi.service.UserService
import dev.jombi.ubi.util.response.GuidedResponse
import dev.jombi.ubi.util.response.GuidedResponseBuilder
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/user")
class UserController(val service: UserService) {
    @GetMapping("/profile")
    fun fetchProfile(p: Principal): ResponseEntity<GuidedResponse<Profile>> {
        val profile = service.getProfileById(UUID.fromString(p.name))
        return ResponseEntity.ok(GuidedResponseBuilder{}.build(profile))
    }

    @GetMapping("/find")
    fun find(@RequestParam("id") request: String): ResponseEntity<GuidedResponse<UserIdAndNameResponse>> {
        val result = service.findUser(request)
        return ResponseEntity.ok(GuidedResponseBuilder { message = "found" }.build(result))
    }
}