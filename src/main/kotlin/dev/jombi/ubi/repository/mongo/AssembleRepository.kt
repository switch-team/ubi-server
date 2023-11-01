package dev.jombi.ubi.repository.mongo

import dev.jombi.ubi.entity.Assemble
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AssembleRepository : MongoRepository<Assemble, String> {
    fun queryAssembleByHostIs(id: UUID): Assemble?
    @Query("{users: {user: ?0}}")
    fun queryAssemblesByUsers(user2: UUID): List<Assemble>

    @Query("{users: {user: user2}}")
    fun queryAssemblesByUsersInUserUUID(user2: UUID): List<Assemble>
}