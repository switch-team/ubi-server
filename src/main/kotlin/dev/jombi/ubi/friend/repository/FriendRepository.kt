package dev.jombi.ubi.friend.repository

import dev.jombi.ubi.friend.entity.Friend
import dev.jombi.ubi.friend.entity.FriendKey
import dev.jombi.ubi.user.entity.User
import dev.jombi.ubi.friend.entity.state.FriendState
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface FriendRepository : JpaRepository<Friend, FriendKey> {
    @Query("SELECT COUNT(f) FROM Friend f WHERE (f.sender = :user OR f.receiver = :user) AND f.state = dev.jombi.ubi.friend.entity.state.FriendState.ACCEPTED")
    fun countFriends(user: User): Int
    @Query("SELECT f FROM Friend f WHERE (f.sender = :user OR f.receiver = :user) AND (f.sender.id = :friend OR f.receiver.id = :friend)")
    fun findRelationshipById(@Param("user") user: User, @Param("friend") f: UUID): Friend?
    @Query("SELECT f FROM Friend f WHERE (f.sender = :user OR f.receiver = :user) AND (f.sender = :friend OR f.receiver = :friend)")
    fun findFriendByTwoUser(@Param("user") user: User, @Param("friend") f: User): Friend?
    @Query("SELECT f FROM Friend f WHERE (f.sender = :user OR f.receiver = :user) AND f.state = :state")
    fun findUsersByUserAndState(@Param("user") sender: User, @Param("state") state: FriendState = FriendState.PENDING): List<Friend>
    @Query("SELECT f FROM Friend f WHERE f.receiver = :user AND f.state = dev.jombi.ubi.friend.entity.state.FriendState.PENDING")
    fun findPendingRequests(@Param("user") sender: User): List<Friend>
}