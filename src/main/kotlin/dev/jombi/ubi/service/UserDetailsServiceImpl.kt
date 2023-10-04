package dev.jombi.ubi.service

import dev.jombi.ubi.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserDetailsServiceImpl(private val userRepository: UserRepository) :
    UserDetailsService {
    override fun loadUserByUsername(uuid: String): UserDetails {
        val user = userRepository.findUserById(UUID.fromString(uuid))
            ?: throw UsernameNotFoundException("User not found.")

        return User(
            "${user.id}",
            user.password,
            user.authorities.map { SimpleGrantedAuthority(it.authorityName) })
    }
}