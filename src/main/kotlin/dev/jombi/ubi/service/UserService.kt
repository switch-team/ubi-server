package dev.jombi.ubi.service

import dev.jombi.ubi.dto.Profile
import dev.jombi.ubi.dto.response.UserIdAndNameResponse
import dev.jombi.ubi.entity.UploadedFile
import dev.jombi.ubi.entity.User
import dev.jombi.ubi.repository.UserRepository
import dev.jombi.ubi.util.response.CustomError
import dev.jombi.ubi.util.response.ErrorDetail
import org.springframework.stereotype.Service
import java.net.URL
import java.util.UUID

@Suppress("unused")
@Service
class UserService(private val userRepository: UserRepository) {
    fun getProfileById(uuid: UUID): Profile {
        val user = userRepository.findUserById(uuid) ?: throw CustomError(ErrorDetail.USER_NOT_FOUND)
        return Profile(user.name, user.phone, user.email, user.profileImage?.url?.let { URL(it) })
    }

    fun updateProfile(user: User, phone: String?, email: String?, name: String?, file: UploadedFile?) {
        if (email != null) userRepository.getUserByEmail(email)?.let { throw CustomError(ErrorDetail.EMAIL_ALREADY_EXISTS) }
        if (phone != null) userRepository.getUserByPhone(phone)?.let { throw CustomError(ErrorDetail.PHONE_ALREADY_EXISTS) }
        val modifiedUser = user.copy(
            phone = phone ?: user.phone,
            email = email ?: user.email,
            name = name ?: user.name,
            profileImage = file ?: user.profileImage
        )
        userRepository.save(modifiedUser)
    }

    fun verifyUserExists(uuid: UUID): Boolean {
        return userRepository.existsById(uuid)
    }

    fun findUser(phoneOrEmail: String): UserIdAndNameResponse {
        val user = (
                if (phoneOrEmail.contains("@")) userRepository.getUserByEmail(phoneOrEmail)
                else userRepository.getUserByPhone(phoneOrEmail.replace("-", ""))
                ) ?: throw CustomError(ErrorDetail.USER_NOT_FOUND)
        return UserIdAndNameResponse(id = user.id.toString(), name = user.name)
    }


    fun getUserById(id: UUID): User {
        return userRepository.findUserById(id) ?: throw CustomError(ErrorDetail.USER_NOT_FOUND)
    }

    fun getUserByPhoneOrEmail(phoneOrEmail: String): User {
        return (
                if (phoneOrEmail.contains("@")) userRepository.getUserByEmail(phoneOrEmail)
                else userRepository.getUserByPhone(phoneOrEmail.replace("-", ""))
                ) ?: throw CustomError(ErrorDetail.USER_NOT_FOUND)
    }
}