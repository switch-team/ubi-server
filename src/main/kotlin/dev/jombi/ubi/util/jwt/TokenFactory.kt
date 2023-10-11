package dev.jombi.ubi.util.jwt

import dev.jombi.ubi.util.response.CustomError
import dev.jombi.ubi.util.response.ErrorDetail
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


const val CLAIM_ID = "JombiJWTAPI"

@Component
class TokenFactory(
    @Value("\${jwt.secret}") val secret: String,
    @Value("\${jwt.token-expires-at}") val expiresAt: Long
) : InitializingBean {
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

    fun getAuthenticationByToken(token: String): Authentication {
        val claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
        val authorities =
            claims[CLAIM_ID].toString().split(",").filter { it.isNotBlank() }.map { SimpleGrantedAuthority(it) }
        val principal = User(claims.subject, "", authorities)
        return UsernamePasswordAuthenticationToken(principal, token, authorities)
    }

    private lateinit var key: Key

    @OptIn(ExperimentalEncodingApi::class)
    override fun afterPropertiesSet() {
        val keyBytes = Base64.decode(secret)
        this.key = Keys.hmacShaKeyFor(keyBytes)
    }

    fun validateToken(token: String): Jws<Claims> {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
        } catch (e: SecurityException) {
            throw CustomError(ErrorDetail.MALFORMED_TOKEN)
        } catch (e: MalformedJwtException) {
            throw CustomError(ErrorDetail.MALFORMED_TOKEN)
        } catch (e: ExpiredJwtException) {
            throw CustomError(ErrorDetail.EXPIRED_TOKEN)
        } catch (e: UnsupportedJwtException) {
            throw CustomError(ErrorDetail.MALFORMED_TOKEN)
        } catch (e: IllegalArgumentException) {
            throw CustomError(ErrorDetail.MALFORMED_TOKEN)
        }
    }
}