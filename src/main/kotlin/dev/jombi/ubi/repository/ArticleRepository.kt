package dev.jombi.ubi.repository

import dev.jombi.ubi.entity.Article
import dev.jombi.ubi.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ArticleRepository: JpaRepository<Article, UUID> {
    fun getArticleById(id: UUID): Article?
    fun getArticlesByWriter(user: User): List<Article>
    @Query("SELECT a FROM Article a") //  WHERE a.date > :target
    fun articlesDate(/*@Param("target") n: LocalDateTime*/): List<Article>
}