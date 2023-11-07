package dev.jombi.ubi.controller

import dev.jombi.ubi.dto.request.PostArticleRequest
import dev.jombi.ubi.dto.response.ArticleTitleAndDateResponse
import dev.jombi.ubi.dto.response.ViewArticleResponse
import dev.jombi.ubi.service.ArticleService
import dev.jombi.ubi.service.FileService
import dev.jombi.ubi.service.UserService
import dev.jombi.ubi.util.UUIDSafe
import dev.jombi.ubi.util.response.GuidedResponse
import dev.jombi.ubi.util.response.GuidedResponseBuilder
import dev.jombi.ubi.websocket.handler.PacketHandler
import jakarta.validation.Valid
import org.loverde.geographiccoordinate.Latitude
import org.loverde.geographiccoordinate.Longitude
import org.loverde.geographiccoordinate.Point
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

@RestController
@RequestMapping("/article")
class ArticleController(
    val articleService: ArticleService,
    val userService: UserService,
    val fileService: FileService,
    val handler: PacketHandler
) {
    @PostMapping("/{id}/like")
    fun likeArticle(@PathVariable id: String, p: Principal): ResponseEntity<GuidedResponse<Any>> {
        val user = userService.getUserById(UUIDSafe(p.name))
        articleService.likeArticle(UUIDSafe(id), user)
        return ResponseEntity.ok(GuidedResponseBuilder {}.noData())
    }

    // TODO: GEOCode the article lists, Time limit
    @GetMapping
    fun getArticles(@RequestParam latitude: Double, @RequestParam longitude: Double,  p: Principal): ResponseEntity<GuidedResponse<List<ArticleTitleAndDateResponse>>> {
        val articles = articleService.getArticles(Point(Latitude(latitude), Longitude(longitude)), 30 * 1000.0)
        return ResponseEntity.ok(GuidedResponseBuilder {}.build(articles))
    }

    @GetMapping("/my")
    fun viewMyArticleList(p: Principal): ResponseEntity<GuidedResponse<List<ArticleTitleAndDateResponse>>> {
        val user = userService.getUserById(UUIDSafe(p.name))
        val result = articleService.viewMyArticleList(user)
        return ResponseEntity.ok(GuidedResponseBuilder {}.build(result))
    }

    @PostMapping
    fun postArticle(
        @RequestPart(value = "thumbnailImage", required = false) file: MultipartFile?,
        @RequestPart(value = "data") @Valid request: PostArticleRequest,
        p: Principal
    ): ResponseEntity<GuidedResponse<Any>> {
        val user = userService.getUserById(UUIDSafe(p.name))
        val article = articleService.postArticle(
            request.title,
            request.content,
            request.latitude,
            request.longitude,
            user,
            file?.let { fileService.upload(file, "article") })
        handler.handleNewArticle(user, article)
        return ResponseEntity.ok(GuidedResponseBuilder {}.noData())
    }

    @GetMapping("/{id}")
    fun viewArticle(
        @PathVariable id: String,
        p: Principal
    ): ResponseEntity<GuidedResponse<ViewArticleResponse>> {
        val result = articleService.getArticle(UUIDSafe(id))
        return ResponseEntity.ok(GuidedResponseBuilder {}.build(result))
    }

    @DeleteMapping("/{id}")
    fun deleteArticle(@PathVariable id: String, p: Principal): ResponseEntity<GuidedResponse<Any>> {
        val user = userService.getUserById(UUIDSafe(p.name))
        articleService.deleteArticle(UUIDSafe(id), user)
        return ResponseEntity.ok(GuidedResponseBuilder {}.noData())
    }
}