package dev.jombi.ubi.entity

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    val id: UUID = UUID.randomUUID(),
    @Column(name = "phone", length = 25, nullable = false)
    val phone: String,
    @Column(name = "email", length = 64, nullable = false)
    val email: String,
    @Column(name = "password", length = 512, nullable = false) // sha-512 encrypted
    val password: String,
    @Column(name = "username", length = 24, nullable = false)
    val name: String,

    @Column(name = "profile_image", length = 256, nullable = true)
    val profileImage: String? = null, // TODO: CDN or INTERNAL FILE

    @ManyToMany
    @JoinTable(
        name = "account_authority",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "authority", referencedColumnName = "authority")]
    )
    val authorities: Set<Authority> = emptySet(),

    @ManyToMany
    @JoinTable(
        name = "friend",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "also_user_id", referencedColumnName = "user_id")]
    )
    val friend: List<User> = emptyList()
)
