package dev.jombi.ubi.article.dto.response

import java.util.UUID

data class ArticleLikeResponse(val articleId: UUID, val liked: Boolean)