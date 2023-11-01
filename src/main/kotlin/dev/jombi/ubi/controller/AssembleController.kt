package dev.jombi.ubi.controller

import dev.jombi.ubi.dto.request.AssembleRequest
import dev.jombi.ubi.service.AssembleService
import dev.jombi.ubi.service.UserService
import dev.jombi.ubi.util.response.GuidedResponse
import dev.jombi.ubi.util.response.GuidedResponseBuilder
import jakarta.websocket.server.PathParam
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import java.util.UUID

@RestController
@RequestMapping("/assemble")
class AssembleController(val assembleService: AssembleService, val userService: UserService) {
//    @GetMapping //초대 리스트 조회
//    fun list(): {}

    @PostMapping("/request")
    fun request(assembleRequest: AssembleRequest, p: Principal): ResponseEntity<GuidedResponse<Any>> {
        val user = userService.getUserById(UUID.fromString(p.name))
        val target = userService.getUserById(assembleRequest.target)
        assembleService.inviteAssemble(user, target, assembleRequest.message)
        return ResponseEntity.ok(GuidedResponseBuilder {}.noData())
    }

    @PostMapping("/{id}")
    fun accept(@PathVariable id: UUID, p: Principal): ResponseEntity<GuidedResponse<Any>> {
        val user = userService.getUserById(UUID.fromString(p.name))
        assembleService.replyAssemble(user, id, true)
        return ResponseEntity.ok(GuidedResponseBuilder {}.noData())
    }

//    @PostMapping("/{id}")
//    fun reject(@PathVariable id: UUID, p: Principal): ResponseEntity<GuidedResponse<>> {
//        val user = userService.getUserById(UUID.fromString(p.name))
//        assembleService.replyAssemble(user, id, true)
//        return ResponseEntity.ok(GuidedResponseBuilder {}.noData())
//    }
}