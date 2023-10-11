package dev.jombi.ubi.dto.response

import dev.jombi.ubi.entity.User
import java.net.URL
import java.util.*

data class ViewArticleResponse(
    val id: UUID,
    val title: String,
    val date: Date,
    val detail: String,
    var viewCount: Long? = null,
    val likeCount: Long,
    val writer: User? = null,
    val thumbnailImage: URL?
)
