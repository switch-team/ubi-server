package dev.jombi.ubi.controller

import dev.jombi.ubi.dto.response.FriendFindResponse
import dev.jombi.ubi.handler.ErrorHandler
import dev.jombi.ubi.service.FriendService
import dev.jombi.ubi.util.response.CustomError
import dev.jombi.ubi.util.response.ErrorDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/friend")
class FriendController(val service: FriendService) {
    @GetMapping("/find")
    fun find(@RequestParam("phone") phone: String): ResponseEntity<FriendFindResponse> {
        val result = service.find(phone)
            ?: throw CustomError(ErrorDetail.USER_NOT_FOUND)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/list")
    fun list() {

    }
}
