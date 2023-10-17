package dev.jombi.ubi.service

import dev.jombi.ubi.entity.User
import dev.jombi.ubi.repository.UserRepository
import dev.jombi.ubi.util.jwt.TokenFactory
import dev.jombi.ubi.util.response.CustomError
import dev.jombi.ubi.util.response.ErrorStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val token: TokenFactory,
    private val authBuilder: AuthenticationManagerBuilder,
    private val encoder: PasswordEncoder,
) {
    fun authenticate(phoneOrEmail: String, password: String): String {
        val user = (
                if (phoneOrEmail.contains("@")) userRepository.getUserByEmail(phoneOrEmail)
                else userRepository.getUserByPhone(phoneOrEmail.replace("-", ""))
                ) ?: throw CustomError(ErrorStatus.USER_NOT_FOUND)


        if (!encoder.matches(password, user.password))
            throw CustomError(ErrorStatus.INCORRECT_PASSWORD)

        val auth =
            authBuilder.`object`.authenticate(UsernamePasswordAuthenticationToken("${user.id}", password))
        SecurityContextHolder.getContext().authentication = auth
        return token.createToken(auth)
    }

    fun registerNew(name: String, email: String, phoneWithDash: String, password: String) {
        val phone = phoneWithDash.replace("-", "")
        userRepository.getUserByEmail(email)?.let { throw CustomError(ErrorStatus.USER_ALREADY_EXISTS) }
        userRepository.getUserByPhone(phone)?.let { throw CustomError(ErrorStatus.USER_ALREADY_EXISTS) }

        val passHashed = encoder.encode(password)

        val user = User(phone = phone, email = email, name = name, password = passHashed)
        userRepository.save(user)
    }
}