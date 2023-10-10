package dev.jombi.ubi.entity

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "board")
data class Board(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),
    @Column(name = "title", length = 180, nullable = false)
    val title: String,
    @Column(name = "detail", length = 3000, nullable = false)
    val detail: String,
    @Column(name = "view")
    val viewCount: Long = 0,
    @Column(name = "like")
    val likeCount: Long= 0,
    @ManyToOne
    val writer: User
)
