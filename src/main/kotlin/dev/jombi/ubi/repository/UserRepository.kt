package dev.jombi.ubi.repository

import dev.jombi.ubi.entity.User
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository: JpaRepository<User, UUID> {
    fun getUserByEmail(email: String): User?
    fun getUserByPhone(phone: String): User?
    @EntityGraph(attributePaths = ["authorities"])
    fun findUserById(id: UUID): User?
}