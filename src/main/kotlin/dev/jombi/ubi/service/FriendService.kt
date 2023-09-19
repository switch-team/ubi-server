package dev.jombi.ubi.service;

import dev.jombi.ubi.dto.response.FriendFindResponse
import dev.jombi.ubi.handler.ErrorHandler
import dev.jombi.ubi.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class FriendService(val repository: UserRepository) {
    fun find(phone: String): FriendFindResponse? {
        val result = repository.findUserByPhone(phone)
        if (result != null) {
            return FriendFindResponse(id = result.id, name = result.name)
        }
        return null
    }
}
