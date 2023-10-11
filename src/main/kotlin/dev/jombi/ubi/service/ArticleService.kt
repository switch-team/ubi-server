package dev.jombi.ubi.service

import dev.jombi.ubi.dto.request.PostBoardRequest
import dev.jombi.ubi.dto.response.ArticleResponse
import dev.jombi.ubi.entity.Article
import dev.jombi.ubi.entity.User
import dev.jombi.ubi.repository.ArticleRepository
import dev.jombi.ubi.util.response.CustomError
import dev.jombi.ubi.util.response.ErrorDetail
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ArticleService(val articleRepository: ArticleRepository) {
    fun likeBoard(id: UUID, auth: Authentication) {


    }

    fun viewArticle(id: UUID, user: User): ArticleResponse {
        val article = articleRepository.getArticleById(id)
            ?: throw CustomError(ErrorDetail.ARTICLE_NOT_FOUND)
        val result = ArticleResponse(
            id = article.id,
            title = article.title,
            detail = article.detail,
            likeCount = article.likeCount
        )
        if (article.writer == user) {
            return result
        }
        result.viewCount = article.viewCount
        return result
    }

    fun postArticle(postBoardRequest: PostBoardRequest, user: User) {
        val article = Article(
            title = postBoardRequest.title,
            detail = postBoardRequest.detail,
            writer = user
        )
        articleRepository.save(article)
    }
}