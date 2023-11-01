package dev.jombi.ubi.service

import dev.jombi.ubi.repository.mongo.AssembleRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AssembleService(private val assembleRepo: AssembleRepository) {
    fun getMyAssemble(id: UUID) {
    }
}