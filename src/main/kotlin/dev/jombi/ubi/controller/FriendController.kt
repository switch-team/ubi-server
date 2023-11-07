package dev.jombi.ubi.controller

import dev.jombi.ubi.dto.request.UserIdRequest
import dev.jombi.ubi.dto.response.UserIdAndNameResponse
import dev.jombi.ubi.dto.response.UserListResponse
import dev.jombi.ubi.service.FriendService
import dev.jombi.ubi.service.UserService
import dev.jombi.ubi.util.UUIDSafe
import dev.jombi.ubi.util.response.GuidedResponse
import dev.jombi.ubi.util.response.GuidedResponseBuilder
import dev.jombi.ubi.websocket.handler.PacketHandler
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/friend")
class FriendController(
    val service: FriendService,
    val userService: UserService,
    val handler: PacketHandler
) {
    @GetMapping
    fun list(auth: Authentication): ResponseEntity<GuidedResponse<UserListResponse>> {
        val user = userService.getUserById(UUIDSafe(auth.name))
        val result = service.getFriendList(user)
        return ResponseEntity.ok(GuidedResponseBuilder { message = "found" }.build(result))
    }

    @GetMapping("/pending")
    fun invitedList(p: Principal): ResponseEntity<GuidedResponse<List<UserIdAndNameResponse>>> {
        val user = userService.getUserById(UUIDSafe(p.name))
        val result = service.getPending(user)
        return ResponseEntity.ok(GuidedResponseBuilder { message = "found" }.build(result))
    }

    @PostMapping("/request")
    fun request(@Valid @RequestBody request: UserIdRequest, auth: Authentication): ResponseEntity<GuidedResponse<Any>> {
        val receiver = userService.getUserById(UUIDSafe(request.id))
        val user = userService.getUserById(UUIDSafe(auth.name))
        service.inviteFriend(user, receiver)
        handler.handleFriendRequest(user.id, receiver.id)
        return ResponseEntity.ok(GuidedResponseBuilder {}.noData())
    }

    @PostMapping("/accept")
    fun accept(@Valid @RequestBody request: UserIdRequest, auth: Authentication): ResponseEntity<GuidedResponse<Any>> {
        val sender = userService.getUserById(UUIDSafe(request.id))
        val user = userService.getUserById(UUIDSafe(auth.name))
        service.acceptFriendRequest(user, sender)
        return ResponseEntity.ok(GuidedResponseBuilder {}.noData())
    }

    @DeleteMapping("/{id}")
    fun delete(p: Principal, @PathVariable id: String): ResponseEntity<GuidedResponse<Any>> {
        val target = userService.getUserById(UUIDSafe(id))
        val user = userService.getUserById(UUIDSafe(p.name))
        service.deleteFriend(user, target)
        return ResponseEntity.ok(GuidedResponseBuilder {}.noData())
    }
}
