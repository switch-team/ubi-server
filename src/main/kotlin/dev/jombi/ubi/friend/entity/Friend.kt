package dev.jombi.ubi.friend.entity

import dev.jombi.ubi.user.entity.User
import dev.jombi.ubi.friend.entity.state.FriendState
import jakarta.persistence.*
import java.util.UUID

@Entity
@IdClass(FriendKey::class)
@Table(name = "friend")
data class Friend(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    val sender: User,

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receiver_id", nullable = false)
    val receiver: User,

    @Column(name = "state", nullable = false)
    val state: FriendState
)