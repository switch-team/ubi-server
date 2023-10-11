package dev.jombi.ubi.dto.response

import dev.jombi.ubi.entity.User
import java.net.URL
import java.time.LocalDateTime
import java.util.*

data class ViewArticleResponse(
    val id: UUID,
    val title: String,
    val date: LocalDateTime,
    val content: String,
    var viewCount: Int,
    val likeCount: Int,
    val writer: User? = null,
    val thumbnailImage: URL?
)
