package dev.jombi.ubi.service

import dev.jombi.ubi.dto.request.PostBoardRequest
import dev.jombi.ubi.entity.Board
import dev.jombi.ubi.entity.User
import dev.jombi.ubi.repository.BoardRepository
import dev.jombi.ubi.util.response.CustomError
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class BoardService(val boardRepository: BoardRepository) {
    fun likeBoard(id: UUID, auth: Authentication) {


    }

    fun viewBoard(id: UUID, auth: Authentication): Board {
        val board = boardRepository.getBoardById(id)
            ?: throw CustomError()
        return
    }

    fun postBoard(postBoardRequest: PostBoardRequest, user: User) {
        val board = Board(
            title = postBoardRequest.title,
            detail = postBoardRequest.detail,
            writer = user
        )
        boardRepository.save(board)
    }
}