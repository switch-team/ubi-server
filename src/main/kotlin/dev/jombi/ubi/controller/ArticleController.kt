package dev.jombi.ubi.controller

import dev.jombi.ubi.dto.request.PostArticleRequest
import dev.jombi.ubi.dto.response.ArticleListResponse
import dev.jombi.ubi.dto.response.ViewArticleResponse
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
        articleService.likeArticle(UUID.fromString(id))
        return ResponseEntity.ok(GuidedResponseBuilder {}.noData())
    }

    @GetMapping
    fun viewMyArticleList(auth: Authentication): ResponseEntity<GuidedResponse<ArticleListResponse>> {
        val user = userService.getUserById(UUID.fromString(auth.name))
        val result = articleService.viewMyArticleList(user)
        return ResponseEntity.ok(GuidedResponseBuilder {}.build(result))
    }

    @PostMapping
    fun postArticle(@Valid @RequestBody postArticleRequest: PostArticleRequest, auth: Authentication): ResponseEntity<GuidedResponse<Any>> {
        val user = userService.getUserById(UUID.fromString(auth.name))
        articleService.postArticle(postArticleRequest, user)
        return ResponseEntity.ok(GuidedResponseBuilder {}.noData())
    }

    @GetMapping("/{id}")
    fun viewArticle(@PathParam("id") id: String, auth: Authentication): ResponseEntity<GuidedResponse<ViewArticleResponse>> {
        val user = userService.getUserById(UUID.fromString(auth.name))
        val result = articleService.viewArticle(UUID.fromString(id), user)
        return ResponseEntity.ok(GuidedResponseBuilder {}.build(result))
    }

    @DeleteMapping("/{id}")
    fun deleteArticle(@PathParam("id") id: String, auth: Authentication): ResponseEntity<GuidedResponse<Any>> {
        val user = userService.getUserById(UUID.fromString(auth.name))
        articleService.deleteArticle(UUID.fromString(id), user)
        return ResponseEntity.ok(GuidedResponseBuilder {}.noData())
    }
}