package dev.jombi.ubi.service

import dev.jombi.ubi.dto.Profile
import dev.jombi.ubi.entity.User
import dev.jombi.ubi.repository.UserRepository
import dev.jombi.ubi.util.response.CustomError
import dev.jombi.ubi.util.response.ErrorDetail
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(private val userRepository: UserRepository) {
    fun getProfileById(uuid: UUID): Profile {
        val user = userRepository.findUserById(uuid) ?: throw CustomError(ErrorDetail.USER_NOT_FOUND)
        return Profile(user.name, user.phone, user.email, user.profileImage)
    }

    fun verifyUserExists(uuid: UUID): Boolean {
        return userRepository.existsById(uuid)
    }
}