package dev.jombi.ubi.entity

import jakarta.persistence.*
import java.util.Date
import java.util.UUID

@Entity
@Table(name = "board")
data class Article(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),
    @Column(name = "date", nullable = false)
    val date: Date = Date(),
    @Column(name = "title", length = 180, nullable = false)
    val title: String,
    @Column(name = "detail", length = 3000, nullable = false)
    val detail: String,
    @Column(name = "view")
    var viewCount: Long = 0,
    @Column(name = "like")
    var likeCount: Long = 0,
    @ManyToOne
    val writer: User
)
