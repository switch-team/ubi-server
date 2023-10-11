package dev.jombi.ubi.entity

import jakarta.persistence.*
import java.util.Date
import java.util.UUID

@Entity
@Table(name = "article")
data class Article(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),
    @Column(name = "date", nullable = false)
    val date: Date = Date(),
    @Column(name = "title", length = 180, nullable = false)
    val title: String,
    @Column(name = "detail", length = 3000, nullable = false)
    val content: String,
    @Column(name = "view")
    val viewCount: Long = 0,
    @Column(name = "like")
    val likeCount: Long = 0,
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    val writer: User,
    @OneToOne(optional = true)
    @JoinColumn(name = "article_image_id", nullable = true)
    val thumbnailImage: UploadedFile? = null
)
