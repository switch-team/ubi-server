package dev.jombi.ubi.entity

import jakarta.persistence.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document("location")
data class Location(
    @Id
    val user: UUID,
    val latitude: Double,
    val longitude: Double,
)