package dev.jombi.ubi.repository

import dev.jombi.ubi.entity.Board
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface BoardRepository: JpaRepository<Board, UUID> {
    val getBoardById(id: String): Board?
}