package dev.jombi.ubi.controller

import dev.jombi.ubi.dto.request.PostArticleRequest
import dev.jombi.ubi.dto.response.ArticleTitleAndDateResponse
import dev.jombi.ubi.dto.response.ViewArticleResponse
import dev.jombi.ubi.service.ArticleService
import dev.jombi.ubi.service.FileService
import dev.jombi.ubi.service.UserService
import dev.jombi.ubi.util.response.GuidedResponse
import dev.jombi.ubi.util.response.GuidedResponseBuilder
import jakarta.validation.Valid
import org.loverde.geographiccoordinate.Latitude
import org.loverde.geographiccoordinate.Longitude
import org.loverde.geographiccoordinate.Point
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.net.URL
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/article")
class ArticleController(
    val articleService: ArticleService,
    val userService: UserService,
    val fileService: FileService
) {
    @PostMapping("/{id}/like")
    fun likeArticle(@PathVariable id: String, p: Principal): ResponseEntity<GuidedResponse<Any>> {
        val user = userService.getUserById(UUID.fromString(p.name))
        articleService.likeArticle(UUID.fromString(id), user)
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
        val user = userService.getUserById(UUID.fromString(p.name))
        val result = articleService.viewMyArticleList(user)
        return ResponseEntity.ok(GuidedResponseBuilder {}.build(result))
    }

    @PostMapping
    fun postArticle(
        @RequestPart(value = "thumbnailImage", required = false) file: MultipartFile?,
        @RequestPart(value = "data") @Valid request: PostArticleRequest,
        p: Principal
    ): ResponseEntity<GuidedResponse<Any>> {
        val user = userService.getUserById(UUID.fromString(p.name))
        articleService.postArticle(
            request.title,
            request.content,
            request.latitude,
            request.longitude,
            user,
            file?.let { fileService.upload(file, "article") })
        return ResponseEntity.ok(GuidedResponseBuilder {}.noData())
    }

    @GetMapping("/{id}")
    fun viewArticle(
        @PathVariable id: String,
        p: Principal
    ): ResponseEntity<GuidedResponse<ViewArticleResponse>> {
        val result = articleService.getArticle(UUID.fromString(id))
        return ResponseEntity.ok(GuidedResponseBuilder {}.build(result))
    }

    @DeleteMapping("/{id}")
    fun deleteArticle(@PathVariable id: String, p: Principal): ResponseEntity<GuidedResponse<Any>> {
        val user = userService.getUserById(UUID.fromString(p.name))
        articleService.deleteArticle(UUID.fromString(id), user)
        return ResponseEntity.ok(GuidedResponseBuilder {}.noData())
    }
}