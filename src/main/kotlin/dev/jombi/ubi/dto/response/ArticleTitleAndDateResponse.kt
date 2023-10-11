package dev.jombi.ubi.dto.response

import java.net.URL
import java.util.Date
import java.util.UUID

data class ArticleTitleAndDateResponse(
    val id: UUID,
    val title: String,
    val date: Date,
    val thumbnailImage: URL?
)
