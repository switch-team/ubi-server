package dev.jombi.ubi.controller

import dev.jombi.ubi.util.response.GuidedResponse
import dev.jombi.ubi.util.response.GuidedResponseBuilder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/test")
class TestController {
    @GetMapping("/get")
    fun getSomething(): GuidedResponse<String> {
        return GuidedResponseBuilder().build("hi")
    }

    @PostMapping("/post")
    fun postSomething(): GuidedResponse<String> {
        return GuidedResponseBuilder().build("some data")
    }
}