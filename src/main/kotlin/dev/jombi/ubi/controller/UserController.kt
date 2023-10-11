package dev.jombi.ubi.controller

import dev.jombi.ubi.dto.Profile
import dev.jombi.ubi.dto.request.ModifyProfileRequest
import dev.jombi.ubi.dto.response.UserIdAndNameResponse
import dev.jombi.ubi.service.FileService
import dev.jombi.ubi.service.UserService
import dev.jombi.ubi.util.response.GuidedResponse
import dev.jombi.ubi.util.response.GuidedResponseBuilder
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/user")
class UserController(val service: UserService, val fileService: FileService) {
    @GetMapping("/profile")
    fun fetchProfile(p: Principal): ResponseEntity<GuidedResponse<Profile>> {
        val profile = service.getProfileById(UUID.fromString(p.name))
        return ResponseEntity.ok(GuidedResponseBuilder {}.build(profile))
    }

    @PatchMapping("/profile")
    fun modifyProfile(
        @RequestPart("profileImage", required = false) file: MultipartFile?,
        @RequestPart("data", required = false) @Valid request: ModifyProfileRequest?,
        p: Principal
    ): ResponseEntity<GuidedResponse<Any>> {
        val user = service.getUserById(UUID.fromString(p.name))
        service.updateProfile(
            user,
            request?.phone,
            request?.email,
            request?.name,
            file?.let { fileService.upload(file, "profile") }
        )
        return ResponseEntity.ok(GuidedResponseBuilder{}.noData())
    }

    @GetMapping("/find")
    fun find(@RequestParam("id") request: String): ResponseEntity<GuidedResponse<UserIdAndNameResponse>> {
        val result = service.findUser(request)
        return ResponseEntity.ok(GuidedResponseBuilder { message = "found" }.build(result))
    }
}