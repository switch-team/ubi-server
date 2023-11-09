package dev.jombi.ubi.dto.response

import java.util.UUID

data class ArticleLikeResponse(val articleId: UUID, val liked: Boolean)