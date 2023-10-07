package dev.jombi.ubi.service

import dev.jombi.ubi.dto.Profile
import dev.jombi.ubi.dto.response.UserIdAndNameResponse
import dev.jombi.ubi.entity.User
import dev.jombi.ubi.repository.UserRepository
import dev.jombi.ubi.util.response.CustomError
import dev.jombi.ubi.util.response.ErrorDetail
import org.springframework.stereotype.Service
import java.lang.RuntimeException
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

    fun findUser(phoneOrEmail: String): UserIdAndNameResponse {
        val user = (
                if (phoneOrEmail.contains("@")) userRepository.getUserByEmail(phoneOrEmail)
                else userRepository.getUserByPhone(phoneOrEmail.replace("-", ""))
                ) ?: throw CustomError(ErrorDetail.USER_NOT_FOUND)
        return UserIdAndNameResponse(id = user.phone, name = user.name)
    }


    fun getUserById(id: UUID): User {
        val user = userRepository.findUserById(id) ?: throw CustomError(ErrorDetail.USER_NOT_FOUND)
        return user
    }

    fun getUserByPhoneOrEmail(phoneOrEmail: String): User {
        val user = (
                if (phoneOrEmail.contains("@")) userRepository.getUserByEmail(phoneOrEmail)
                else userRepository.getUserByPhone(phoneOrEmail.replace("-", ""))
                ) ?: throw CustomError(ErrorDetail.USER_NOT_FOUND)
        return user
    }
}