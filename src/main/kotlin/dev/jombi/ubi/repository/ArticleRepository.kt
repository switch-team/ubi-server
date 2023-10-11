package dev.jombi.ubi.repository

import dev.jombi.ubi.entity.Article
import dev.jombi.ubi.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ArticleRepository: JpaRepository<Article, UUID> {
    fun getArticleById(id: UUID): Article?
    fun getArticlesByWriter(user: User): List<Article>
}