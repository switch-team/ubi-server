package dev.jombi.ubi.entity

import jakarta.persistence.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document(collation = "assemble")
data class Assemble(
    @Id
    val assembleId: String,
    val title: String,
    val host: UUID,
    val users: List<AssembleUser>,
)