package dev.jombi.ubi.article.entity

import dev.jombi.ubi.file.entity.UploadedFile
import dev.jombi.ubi.user.entity.User
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

/*
 * TODO: GEO Code the article.
 *  - get latitude and longitude and save it in entity.
 *  - Filter it using Calculation.
 *  - Show all if requested user is me.
 **/
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

    @Column
    val latitude: Double,

    @Column
    val longitude: Double,

    @ManyToMany
    @JoinTable(
        name = "article_likes",
        joinColumns = [JoinColumn(name = "article_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "user_id", referencedColumnName = "user_id")]
    )
    val likedUser: Set<User> = emptySet(),

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    val writer: User,

    @OneToOne(optional = true)
    @JoinColumn(name = "article_image_id", nullable = true)
    val thumbnailImage: UploadedFile? = null
)
