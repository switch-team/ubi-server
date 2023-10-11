package dev.jombi.ubi.service

import dev.jombi.ubi.dto.request.PostArticleRequest
import dev.jombi.ubi.dto.response.ArticleListResponse
import dev.jombi.ubi.dto.response.ArticleTitleAndDateResponse
import dev.jombi.ubi.dto.response.ViewArticleResponse
import dev.jombi.ubi.entity.Article
import dev.jombi.ubi.entity.UploadedFile
import dev.jombi.ubi.entity.User
import dev.jombi.ubi.repository.ArticleRepository
import dev.jombi.ubi.util.response.CustomError
import dev.jombi.ubi.util.response.ErrorDetail
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.net.URL
import java.util.UUID

@Service
class ArticleService(val articleRepository: ArticleRepository) {
    fun likeArticle(id: UUID) {
        val article = articleRepository.getArticleById(id)
            ?: throw CustomError(ErrorDetail.ARTICLE_NOT_FOUND)
        article.likeCount += 1
        articleRepository.save(article)
    }

    fun viewMyArticleList(user: User): ArticleListResponse {
        val articles = articleRepository.getArticlesByWriter(user)
        if (articles.isEmpty()) throw CustomError(ErrorDetail.USER_DO_NOT_HAVE_ARTICLE)
        return ArticleListResponse(articles.map {ArticleTitleAndDateResponse(it.id, it.title, it.date, it.thumbnailImage?.url?.let { URL(it) })})
    }

    fun viewArticle(id: UUID, user: User): ViewArticleResponse {
        val article = articleRepository.getArticleById(id)
            ?: throw CustomError(ErrorDetail.ARTICLE_NOT_FOUND)
        val result = ViewArticleResponse(
            id = article.id,
            date = article.date,
            title = article.title,
            detail = article.detail,
            likeCount = article.likeCount,
            thumbnailImage = article.thumbnailImage?.url?.let { URL(it) }
        )
        if (article.writer == user) {
            result.viewCount = article.viewCount
            articleRepository.save(article)
            return result
        }
        article.viewCount += 1
        articleRepository.save(article)
        return result
    }


    fun postArticle(postArticleRequest: PostArticleRequest, user: User, file: UploadedFile?) {
        val article = Article(
            title = postArticleRequest.title,
            detail = postArticleRequest.detail,
            writer = user,
            thumbnailImage = file
        )
        articleRepository.save(article)
    }

    fun deleteArticle(id: UUID, user: User) {
        val article = articleRepository.getArticleById(id)
            ?: throw CustomError(ErrorDetail.ARTICLE_NOT_FOUND)
        if (article.writer != user) throw CustomError(ErrorDetail.USER_IS_NOT_WRITER)
        articleRepository.delete(article)
    }
}