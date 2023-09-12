package dev.jombi.ubi.entity

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID,
    @Column(name = "phone", length = 25)
    val phone: String,
    @Column(name = "email", length = 64)
    val email: String,
    @Column(name = "password", length = 256) // sha-256 encrypted
    val password: String,
    @Column(name = "username", length = 24)
    val name: String
)
