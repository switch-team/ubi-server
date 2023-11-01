package dev.jombi.ubi.repository.mongo

import dev.jombi.ubi.entity.Assemble
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AssembleRepository : MongoRepository<Assemble, String> {
    fun queryAssembleByHostIs(id: UUID): Assemble?
}