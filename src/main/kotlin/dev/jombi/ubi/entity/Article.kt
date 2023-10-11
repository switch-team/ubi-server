package dev.jombi.ubi.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "article")
data class Article(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),
    @Column(name = "date", nullable = false)
    val date: LocalDateTime = LocalDateTime.now(),
    @Column(name = "title", length = 128, nullable = false)
    val title: String,
    @Column(name = "content", length = 2048, nullable = false)
    val content: String,
    @Column(name = "view_count")
    val viewCount: Int = 0,
    @Column(name = "like_count")
    val likeCount: Int = 0,
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    val writer: User,
    @OneToOne(optional = true)
    @JoinColumn(name = "article_image_id", nullable = true)
    val thumbnailImage: UploadedFile? = null
)
