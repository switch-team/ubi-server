package dev.jombi.ubi.entity

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "user_image")
data class UserImage (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID()
)