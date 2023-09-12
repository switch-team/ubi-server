package dev.jombi.ubi.util.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Component
import java.security.Key
import java.util.Date
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

const val CLAIM_ID = "JombiJWTAPI"

@Component
class TokenFactory(@Value("\${jwt.secret}") val secret: String, @Value("\${jwt.token-expires-at}") val expiresAt: Long) : InitializingBean {
    fun createToken(auth: Authentication): String {
        val authorities = auth.authorities.joinToString(",", transform = GrantedAuthority::getAuthority)
        val date = Date(System.currentTimeMillis() + expiresAt)
        return Jwts.builder()
            .setSubject(auth.name)
            .claim(CLAIM_ID, authorities)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(date)
            .compact()
    }

    private lateinit var key: Key

    @OptIn(ExperimentalEncodingApi::class)
    override fun afterPropertiesSet() {
        val keyBytes = Base64.decode(secret)
        this.key = Keys.hmacShaKeyFor(keyBytes)
    }
}