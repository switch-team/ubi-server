package dev.jombi.ubi.controller

import dev.jombi.ubi.dto.request.UserIdRequest
import dev.jombi.ubi.dto.response.PendingResponse
import dev.jombi.ubi.dto.response.UserListResponse
import dev.jombi.ubi.service.FriendService
import dev.jombi.ubi.service.UserService
import dev.jombi.ubi.util.response.GuidedResponse
import dev.jombi.ubi.util.response.GuidedResponseBuilder
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/friend")
class FriendController(val service: FriendService, val userService: UserService) {
    @GetMapping
    fun list(auth: Authentication): ResponseEntity<GuidedResponse<UserListResponse>> {
        val user = userService.getUserById(UUID.fromString(auth.name))
        val result = service.getFriendList(user)
        return ResponseEntity.ok(GuidedResponseBuilder { message = "found" }.build(result))
    }

    @GetMapping("/pending")
    fun invitedList(auth: Authentication): ResponseEntity<GuidedResponse<List<PendingResponse>>> {
        val user = userService.getUserById(UUID.fromString(auth.name))
        val result = service.getPending(user)
        return ResponseEntity.ok(GuidedResponseBuilder { message = "found" }.build(result))
    }

    @PostMapping("/request")
    fun request(@Valid @RequestBody request: UserIdRequest, auth: Authentication): ResponseEntity<GuidedResponse<Any>> {
        val receiver = userService.getUserById(UUID.fromString(request.id))
        val user = userService.getUserById(UUID.fromString(auth.name))
        service.inviteFriend(user, receiver)
        return ResponseEntity.ok(GuidedResponseBuilder{}.noData())
    }

    @PostMapping("/accept")
    fun accept(@Valid @RequestBody request: UserIdRequest, auth: Authentication): ResponseEntity<GuidedResponse<Any>> {
        val sender = userService.getUserById(UUID.fromString(request.id))
        val user = userService.getUserById(UUID.fromString(auth.name))
        service.acceptFriendRequest(user, sender)
        return ResponseEntity.ok(GuidedResponseBuilder{}.noData())
    }

    @DeleteMapping("/{id}")
    fun delete(auth: Authentication, @PathVariable id: String): ResponseEntity<GuidedResponse<Any>> {
        val user = userService.getUserById(UUID.fromString(auth.name))
        val target = userService.getUserById(UUID.fromString(id))
        service.deleteFriend(user, target)
        return ResponseEntity.ok(GuidedResponseBuilder{}.noData())
    }
}
