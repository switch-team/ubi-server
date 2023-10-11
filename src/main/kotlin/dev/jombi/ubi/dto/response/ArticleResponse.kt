package dev.jombi.ubi.dto.response

import dev.jombi.ubi.entity.User
import jakarta.persistence.*
import java.util.*

data class ArticleResponse(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val detail: String,
    val viewCount: Long? = null,
    val likeCount: Long,
    val writer: User? = null
)
