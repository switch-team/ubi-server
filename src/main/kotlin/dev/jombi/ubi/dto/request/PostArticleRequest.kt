package dev.jombi.ubi.dto.request

import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull

data class PostArticleRequest(
    @field:NotNull
    @field:NotBlank
    val title: String,
    @field:NotNull
    @field:NotBlank
    val detail: String
)
