package dev.jombi.ubi.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/assemble")
class AssembleController {
    @GetMapping //초대 리스트 조회
    fun list():
}