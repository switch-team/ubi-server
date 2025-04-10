package dev.jombi.ubi.service

import dev.jombi.ubi.dto.response.ArticleTitleAndDateResponse
import dev.jombi.ubi.dto.response.ViewArticleResponse
import dev.jombi.ubi.entity.Article
import dev.jombi.ubi.entity.UploadedFile
import dev.jombi.ubi.entity.User
import dev.jombi.ubi.repository.ArticleRepository
import dev.jombi.ubi.util.response.CustomError
import dev.jombi.ubi.util.response.ErrorStatus
import org.loverde.geographiccoordinate.Latitude
import org.loverde.geographiccoordinate.Longitude
import org.loverde.geographiccoordinate.Point
import org.loverde.geographiccoordinate.calculator.DistanceCalculator
import org.springframework.stereotype.Service
import java.net.URL
import java.time.LocalDateTime
import java.util.UUID

@Service
class ArticleService(val articleRepository: ArticleRepository) {
    fun likeArticle(id: UUID, user: User) {
        val article = articleRepository.getArticleById(id)
            ?: throw CustomError(ErrorStatus.ARTICLE_NOT_FOUND)
        if (article.writer == user)
            throw CustomError(ErrorStatus.CANT_LIKE_OWN_ARTICLE)
        if (article.likedUser.contains(user))
            throw CustomError(ErrorStatus.ALREADY_LIKED)
        articleRepository.save(article.copy(likedUser = article.likedUser + user))
    }

    fun getArticles(point: Point, distanceMeter: Double): List<ArticleTitleAndDateResponse> {
        val oneDayBefore = LocalDateTime.now().minusDays(1)
        return articleRepository.articlesDate(oneDayBefore)
            .filter {
                DistanceCalculator.distance(
                    DistanceCalculator.Unit.METERS,
                    Point(Latitude(it.latitude), Longitude(it.longitude)),
                    point
                ) <= distanceMeter
            }
            .map {
                ArticleTitleAndDateResponse(
                    it.id,
                    it.title,
                    it.date,
                    it.latitude,
                    it.longitude,
                    it.thumbnailImage?.url?.let { u -> URL(u) })
            }
    }

    fun viewMyArticleList(user: User): List<ArticleTitleAndDateResponse> {
        val articles = articleRepository.getArticlesByWriter(user)
        if (articles.isEmpty()) return emptyList()
        return articles.map {
            ArticleTitleAndDateResponse(
                it.id,
                it.title,
                it.date,
                it.latitude,
                it.longitude,
                it.thumbnailImage?.url?.let { u -> URL(u) })
        }
    }

    fun getArticle(id: UUID, update: Boolean = true): ViewArticleResponse {
        var article = articleRepository.getArticleById(id)
            ?: throw CustomError(ErrorStatus.ARTICLE_NOT_FOUND)

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


    fun postArticle(
        title: String,
        content: String,
        latitude: Double,
        longitude: Double,
        user: User,
        file: UploadedFile?
    ) {
        val article = Article(
            title = title, // don't get request object directly
            content = content,
            writer = user,
            latitude = latitude,
            longitude = longitude,
            thumbnailImage = file
        )
        articleRepository.save(article)
    }

    fun deleteArticle(id: UUID, user: User) {
        val article = articleRepository.getArticleById(id)
            ?: throw CustomError(ErrorStatus.ARTICLE_NOT_FOUND)
        if (article.writer != user) throw CustomError(ErrorStatus.USER_IS_NOT_WRITER)
        articleRepository.delete(article)
    }
}