package dev.jombi.ubi.websocket.repository

import dev.jombi.ubi.websocket.entity.Location
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface LocationRepository : MongoRepository<Location, UUID> {

}