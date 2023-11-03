package dev.jombi.ubi.controller

import dev.jombi.ubi.dto.request.assemble.AssembleHostRequest
import dev.jombi.ubi.dto.request.assemble.AssembleInviteAndJoinRequest
import dev.jombi.ubi.dto.request.assemble.AssembleJoinRequest
import dev.jombi.ubi.entity.Assemble
import dev.jombi.ubi.service.AssembleService
import dev.jombi.ubi.service.UserService
import dev.jombi.ubi.util.response.CustomError
import dev.jombi.ubi.util.response.ErrorStatus
import dev.jombi.ubi.util.response.GuidedResponse
import dev.jombi.ubi.util.response.GuidedResponseBuilder
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/assemble")
class AssembleController(private val service: AssembleService, private val userService: UserService) {
    @PostMapping("/invite")
    fun inviteAssemble(
        p: Principal,
        @Valid @RequestBody request: AssembleInviteAndJoinRequest
    ): ResponseEntity<GuidedResponse<Assemble>> {
        val user = userService.getUserById(UUID.fromString(p.name))
        val target = UUID.fromString(request.id)
        userService.verifyUserExists(target)
        val assembleInfo = service.inviteAssemble(user, target, request.message ?: "")
        return ResponseEntity.ok(GuidedResponseBuilder {}.build(assembleInfo))
    }

    @PostMapping("/host")
    fun hostAssemble(
        p: Principal,
        @Valid @RequestBody request: AssembleHostRequest
    ): ResponseEntity<GuidedResponse<Assemble>> {
        val user = userService.getUserById(UUID.fromString(p.name))
        val assembleInfo = service.createAssemble(user, request.title)
        return ResponseEntity.ok(GuidedResponseBuilder {}.build(assembleInfo))
    }

    @GetMapping("/{id}")
    fun assembleInfo(
        p: Principal,
        @PathVariable id: String,
    ): ResponseEntity<GuidedResponse<Assemble>> {
        val assembleHost = try {
            UUID.fromString(id)
        } catch (e: IllegalArgumentException) {
            throw CustomError(ErrorStatus.INVALID_PATH_VARIABLE)
        }
        val user = userService.getUserById(UUID.fromString(p.name))
        return ResponseEntity.ok(GuidedResponseBuilder {  }.build(service.getAssembleInfoById(user, assembleHost)))
    }

    @GetMapping
    fun assembles(p: Principal): ResponseEntity<GuidedResponse<List<Assemble>>> {
        val user = userService.getUserById(UUID.fromString(p.name))
        return ResponseEntity.ok(GuidedResponseBuilder {  }.build(service.getRelatedAssembles(user.id)))
    }

    @PostMapping("/{id}")
    fun replyAssemble(
        p: Principal,
        @PathVariable id: String,
        @Valid @RequestBody request: AssembleJoinRequest
    ): ResponseEntity<GuidedResponse<Assemble>> {
        val assembleHost = try {
            UUID.fromString(id)
        } catch (e: IllegalArgumentException) {
            throw CustomError(ErrorStatus.INVALID_PATH_VARIABLE)
        }
        val user = userService.getUserById(UUID.fromString(p.name))
        val assembleInfo = service.replyAssemble(user, assembleHost, request.accept)
        return ResponseEntity.ok(GuidedResponseBuilder {}.build(assembleInfo))
    }

    @GetMapping("/join")
    fun join(p: Principal): ResponseEntity<GuidedResponse<List<Assemble>>> {
        val user = userService.getUserById(UUID.fromString(p.name))
        return ResponseEntity.ok(GuidedResponseBuilder {  }.build(service.getRelatedJoin(user)))
    }

    @PostMapping("/join")
    fun joinRequest(
        p: Principal,
        @Valid @RequestBody request: AssembleInviteAndJoinRequest
    ): ResponseEntity<GuidedResponse<Assemble>> {
        val user = userService.getUserById(UUID.fromString(p.name))
        val target = userService.getUserById(UUID.fromString(request.id))
        val assembleInfo = service.requestJoin(user, target, request.message ?: "")
        return ResponseEntity.ok(GuidedResponseBuilder {}.build(assembleInfo))
    }


    @PostMapping("/join/{id}")
    fun replyJoin(
        p: Principal,
        @PathVariable id: String,
        @Valid @RequestBody request: AssembleJoinRequest
    ): ResponseEntity<GuidedResponse<Assemble>> {
        val assembleHost = userService.getUserById(UUID.fromString(p.name)) //host가 본인
        val user = userService.getUserById( try {
            UUID.fromString(id)
        } catch (e: IllegalArgumentException) {
            throw CustomError(ErrorStatus.INVALID_PATH_VARIABLE)
        })
        val assembleInfo = service.replyJoin(assembleHost, user, request.accept)
        return ResponseEntity.ok(GuidedResponseBuilder {  }.build(assembleInfo))
    }
}