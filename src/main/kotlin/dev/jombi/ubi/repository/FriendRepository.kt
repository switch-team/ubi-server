package dev.jombi.ubi.repository

import dev.jombi.ubi.entity.Friend
import dev.jombi.ubi.entity.FriendKey
import dev.jombi.ubi.entity.User
import dev.jombi.ubi.util.state.FriendState
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface FriendRepository : JpaRepository<Friend, FriendKey> {
    @Query("SELECT f FROM Friend f WHERE (f.sender = :user OR f.receiver = :user) AND (f.sender = :friend OR f.receiver = :friend)")
    fun findFriendByTwoUser(@Param("user") user: User, @Param("friend") f: User): Friend? // 친구가 되었을 때, 관계를 구하기 위해서
    @Query("SELECT f FROM Friend f WHERE (f.sender = :user OR f.receiver = :user) AND f.state = :state")
    fun findUsersByUserAndState(@Param("user") sender: User, @Param("state") state: FriendState = FriendState.PENDING): List<Friend>
}