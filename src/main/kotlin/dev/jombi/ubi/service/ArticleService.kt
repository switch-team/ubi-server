package dev.jombi.ubi.service

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
    fun likeArticle(id: UUID, user: User) {
        val article = articleRepository.getArticleById(id)
            ?: throw CustomError(ErrorDetail.ARTICLE_NOT_FOUND)
        if (article.writer == user)
            throw CustomError(ErrorDetail.CANT_LIKE_OWN_ARTICLE)
        if (article.likedUser.contains(user))
            throw CustomError(ErrorDetail.ALREADY_LIKED)
        articleRepository.save(article.copy(likedUser = article.likedUser + user))
    }

    fun viewMyArticleList(user: User): List<ArticleTitleAndDateResponse> {
        val articles = articleRepository.getArticlesByWriter(user)
        if (articles.isEmpty()) throw CustomError(ErrorDetail.USER_DO_NOT_HAVE_ARTICLE)
        return articles.map {
            ArticleTitleAndDateResponse(
                it.id,
                it.title,
                it.date,
                it.thumbnailImage?.url?.let { u -> URL(u) })
        }
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
            likeCount = article.likedUser.size,
            viewCount = article.viewCount,
            writer = article.writer.name,
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