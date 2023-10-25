package dev.jombi.ubi.dto.response

import java.net.URL
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID

data class ArticleTitleAndDateResponse(
    val id: UUID,
    val title: String,
    val date: LocalDateTime,
    val latitude: Double,
    val longitude: Double,
    val thumbnailImage: URL?
)
