package dev.jombi.ubi.repository

import dev.jombi.ubi.entity.Article
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ArticleRepository: JpaRepository<Article, UUID> {
    fun getArticleById(id: UUID): Article?
}