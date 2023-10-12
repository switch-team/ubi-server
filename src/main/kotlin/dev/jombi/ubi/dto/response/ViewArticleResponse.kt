package dev.jombi.ubi.dto.response

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
    val writer: String, // MUST BE NAME, OR IT WILL BE REVEAL ENCRYPTED PASSWORD
    val thumbnailImage: URL?
)
