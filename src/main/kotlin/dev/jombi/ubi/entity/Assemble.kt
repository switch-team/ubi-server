package dev.jombi.ubi.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document("assemble")
data class Assemble(
    @Id
    val host: UUID,
    val title: String,
    val users: List<AssembleUser>,
)