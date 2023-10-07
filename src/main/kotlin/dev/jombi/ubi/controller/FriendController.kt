package dev.jombi.ubi.controller

import dev.jombi.ubi.dto.request.UserIdRequest
import dev.jombi.ubi.dto.response.UserIdAndNameResponse
import dev.jombi.ubi.dto.response.UserListResponse
import dev.jombi.ubi.service.FriendService
import dev.jombi.ubi.service.UserService
import dev.jombi.ubi.util.response.GuidedResponse
import dev.jombi.ubi.util.response.GuidedResponseBuilder
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/friend")
class FriendController(val service: FriendService, val userService: UserService) {
    @GetMapping("/list")
    fun list(auth: Authentication): ResponseEntity<GuidedResponse<UserListResponse>> {
        val user = userService.getUserById(UUID.fromString(auth.name))
//            ?: throw CustomError(ErrorDetail.USER_NOT_FOUND) // 회원 탈퇴 후에도 JWT 토큰이 남아있을 가능성 존재
        val result = service.getFriendList(user)
        return ResponseEntity.ok(GuidedResponseBuilder { message = "found" }.build(result))
    }


    //초대 건거
    @GetMapping("/invited-list")
    fun invitedList(auth: Authentication): ResponseEntity<GuidedResponse<UserListResponse>> {
        val user = userService.getUserById(UUID.fromString(auth.name))
        val result = service.getInvitedList(user)
        return ResponseEntity.ok(GuidedResponseBuilder { message = "found" }.build(result))
    }
    @GetMapping("/received-list")
    fun receivedList(auth: Authentication): ResponseEntity<GuidedResponse<UserListResponse>> {
        val user = userService.getUserById(UUID.fromString(auth.name))
        val result = service.getReceivedList(user)
        return ResponseEntity.ok(GuidedResponseBuilder { message = "found" }.build(result))
    }

    @PostMapping("/invite")
    fun invite(@RequestBody request: UserIdRequest, auth: Authentication): ResponseEntity<GuidedResponse<Any>> {
        val invitedUser = userService.getUserByPhoneOrEmail(request.id)
        val user = userService.getUserById(UUID.fromString(auth.name))
        service.inviteFriend(invitedUser, user)
        return ResponseEntity.ok(GuidedResponseBuilder { message = "succes" }.noData())
    }

    @PostMapping("/accept")
    fun accept(@RequestBody request: UserIdRequest, auth: Authentication): ResponseEntity<GuidedResponse<Any>> {
        val invitedUser = userService.getUserByPhoneOrEmail(request.id)
        val user = userService.getUserById(UUID.fromString(auth.name))
        service.acceptFriendRequest(invitedUser, user)
        return ResponseEntity.ok(GuidedResponseBuilder { message = "succes" }.noData())
    }

    @PostMapping("/reject")
    fun reject(@RequestBody request: UserIdRequest, auth: Authentication): ResponseEntity<GuidedResponse<Any>> {
        val invitedUser = userService.getUserByPhoneOrEmail(request.id)
        val user = userService.getUserById(UUID.fromString(auth.name))
        service.deleteFriend(invitedUser, user)
        return ResponseEntity.ok(GuidedResponseBuilder { message = "succes" }.noData())
    }

    @DeleteMapping("/delete")
    fun delete(@RequestBody request: UserIdRequest, auth: Authentication): ResponseEntity<GuidedResponse<Any>> {
        val deleteFriend = userService.getUserByPhoneOrEmail(request.id)
        val user = userService.getUserById(UUID.fromString(auth.name))
        service.deleteFriend(deleteFriend, user)
        return ResponseEntity.ok(GuidedResponseBuilder { message = "succes" }.noData())
    }
//    @PostMapping("/invite")
//    fun invite(@RequestBody request: FriendInviteRequest, auth: Authentication): ResponseEntity<GuidedResponse<Any>> {
//        val invitedUser = userService.getUserByPhoneOrEmail(request.id)
//        val user = userService.getUserById(UUID.fromString(auth.name))
//        service.inviteFriend(invitedUser, user)
//        return ResponseEntity.ok(GuidedResponseBuilder { message = "secces" }.noData())
//    }
//
//    @PostMapping("/recive")
//    fun recive(@RequestBody request: FriendReciveRequest, auth: Authentication): ResponseEntity<GuidedResponse<Any>> {
//        val recivedUser = userService.getUserByPhoneOrEmail(request.id)
//        val user = userService.getUserById(UUID.fromString(auth.name))
//        service.reciveFriend(recivedUser, user)
//        return ResponseEntity.ok(GuidedResponseBuilder { message = "recived" }.noData())
//    }
}
