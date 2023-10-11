package dev.jombi.ubi.controller

import dev.jombi.ubi.dto.request.PostBoardRequest
import dev.jombi.ubi.service.ArticleService
import dev.jombi.ubi.service.UserService
import dev.jombi.ubi.util.response.GuidedResponse
import dev.jombi.ubi.util.response.GuidedResponseBuilder
import jakarta.validation.Valid
import jakarta.websocket.server.PathParam
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/board")
class ArticleController(val articleService: ArticleService, val userService: UserService) {
    @PostMapping("/{id}/like")
    fun likeArticle(@PathParam("id") id: String, auth: Authentication): ResponseEntity<GuidedResponse<Any>> {

        return ResponseEntity.ok()
    }

    @GetMapping
    fun myArticleList(auth: Authentication): ResponseEntity<GuidedResponse<Any>> {
        return ResponseEntity.ok()
    }

    @PostMapping
    fun postArticle(@Valid @RequestBody postBoardRequest: PostBoardRequest, auth: Authentication): ResponseEntity<GuidedResponse<Any>> {
        val user = userService.getUserById(UUID.fromString(auth.name))
        articleService.postArticle(postBoardRequest, user)
        return ResponseEntity.ok(GuidedResponseBuilder {}.noData())
    }

    @GetMapping("/{id}")
    fun viewArticle(@PathParam("id") id: String, auth: Authentication): ResponseEntity<GuidedResponse<Any>> {
        val user = userService.getUserById(UUID.fromString(auth.name))
        val result = articleService.viewArticle(UUID.fromString(id), user)
        return ResponseEntity.ok()
    }

    @DeleteMapping("/{id}")
    fun deleteArticle(@PathParam("id") id: String, auth: Authentication): ResponseEntity<GuidedResponse<Any>> {

        return ResponseEntity.ok()
    }
}