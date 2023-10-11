package dev.jombi.ubi.service

import dev.jombi.ubi.dto.response.ArticleListResponse
import dev.jombi.ubi.dto.response.ArticleTitleAndDateResponse
import dev.jombi.ubi.dto.response.ViewArticleResponse
import dev.jombi.ubi.entity.Article
import dev.jombi.ubi.entity.UploadedFile
import dev.jombi.ubi.entity.User
import dev.jombi.ubi.repository.ArticleRepository
import dev.jombi.ubi.util.response.CustomError
import dev.jombi.ubi.util.response.ErrorDetail
import org.springframework.stereotype.Service
import java.net.URL
import java.util.UUID

@Service
class ArticleService(val articleRepository: ArticleRepository) {
    @Suppress("unused")
    fun likeArticle(id: UUID) {
        val article = articleRepository.getArticleById(id)
            ?: throw CustomError(ErrorDetail.ARTICLE_NOT_FOUND)
        articleRepository.save(article.copy(likeCount = article.likeCount + 1)) // must copy
    }

    fun viewMyArticleList(user: User): ArticleListResponse {
        val articles = articleRepository.getArticlesByWriter(user)
        if (articles.isEmpty()) throw CustomError(ErrorDetail.USER_DO_NOT_HAVE_ARTICLE)
        return ArticleListResponse(articles.map {
            ArticleTitleAndDateResponse(
                it.id,
                it.title,
                it.date,
                it.thumbnailImage?.url?.let { URL(it) })
        })
    }

    fun getArticle(id: UUID, update: Boolean = true): ViewArticleResponse {
        var article = articleRepository.getArticleById(id)
            ?: throw CustomError(ErrorDetail.ARTICLE_NOT_FOUND)

        if (update)
            article = articleRepository.save(article.copy(viewCount = article.viewCount + 1))

        return ViewArticleResponse(
            id = article.id,
            date = article.date,
            title = article.title,
            content = article.content,
            likeCount = article.likeCount,
            viewCount = article.viewCount,
            thumbnailImage = article.thumbnailImage?.url?.let { URL(it) }
        )
    }


    fun postArticle(title: String, content: String, user: User, file: UploadedFile?) {
        val article = Article(
            title = title, // don't get request object directly
            content = content,
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