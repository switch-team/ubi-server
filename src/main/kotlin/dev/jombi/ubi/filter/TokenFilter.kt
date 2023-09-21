package dev.jombi.ubi.filter

import dev.jombi.ubi.util.jwt.TokenFactory
import dev.jombi.ubi.util.response.CustomError
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.slf4j.MarkerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean

class TokenFilter(private val tokenFactory: TokenFactory) : GenericFilterBean() {
    private val MARKER = MarkerFactory.getMarker("TokenFilter")
    private val LOGGER = LoggerFactory.getLogger(TokenFilter::class.java)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        request as HttpServletRequest

        extractToken(request)?.let {
            val auth = tokenFactory.getAuthenticationByToken(it)
            SecurityContextHolder.getContext().authentication = auth
            LOGGER.info(MARKER, "[{}] Successfully authenticated.", auth.name)
        }

        chain.doFilter(request, response)
    }

    private fun extractToken(request: HttpServletRequest): String? {
        try {
            val header = request.getHeader("Authorization")
            if (header.isNotBlank() && header.lowercase().startsWith("bearer "))
                return header.drop(7)
        } catch (e: Exception) {
            //
        }
        return null
    }
}