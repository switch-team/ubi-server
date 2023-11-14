package dev.jombi.ubi.assemble.repository

import dev.jombi.ubi.assemble.entity.Assemble
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AssembleRepository : MongoRepository<Assemble, UUID> {
    fun queryAssembleByHostIs(host: UUID): Assemble?
    @Query("{\$or: [{ 'users.user': {\$in: [?0]} }, { 'host': ?0 }]}")
    fun queryRelatedAssemble(id: UUID): List<Assemble>

}