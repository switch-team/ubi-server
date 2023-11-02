package dev.jombi.ubi.repository.mongo

import dev.jombi.ubi.entity.Location
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface LocationRepository : MongoRepository<Location, UUID> {

}