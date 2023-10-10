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
    @Column(name = "password", length = 60, nullable = false) // sha-512 encrypted
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

//    //내가 친추 받은거
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//        name = "recive",
//        joinColumns = [JoinColumn(name = "receiver_id", referencedColumnName = "user_id")],
//        inverseJoinColumns = [JoinColumn(name = "sender_id", referencedColumnName = "user_id")]
//    )
//    val receive: MutableList<User> = mutableListOf()

//    @OneToMany
//    val waitFriends: MutableList<FriendRequest> = mutableListOf(), //내가 보넨 친구
//
//    @OneToMany
//    val invitedFriends: MutableList<User> = mutableListOf() //받은 친구

)
