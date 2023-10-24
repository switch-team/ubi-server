package dev.jombi.ubi.dto.request

import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull

data class PostArticleRequest(
    @field:NotNull
    @field:NotBlank(message = "Must not be blank: title")
    val title: String,
    @field:NotNull
    @field:NotBlank(message = "Must not be blank: content")
    val content: String,
    @field:NotNull
    val latitude: Double,
    @field:NotNull
    val longitude: Double
)
