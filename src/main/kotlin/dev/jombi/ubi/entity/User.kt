package dev.jombi.ubi.entity

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID,
    @Column(name = "phone", length = 25, nullable = false)
    val phone: String,
    @Column(name = "email", length = 64, nullable = false)
    val email: String,
    @Column(name = "password", length = 256, nullable = false) // sha-256 encrypted
    val password: String,
    @Column(name = "username", length = 24, nullable = false)
    val name: String
)
