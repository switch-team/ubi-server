package dev.jombi.ubi.filter

import dev.jombi.ubi.util.jwt.TokenFactory
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean

@Component
class TokenFilter(private val tokenFactory: TokenFactory, @Value("\${jwt.header}") private val header: String) : GenericFilterBean() {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        request as HttpServletRequest

        request.getHeader(header)?.let {
            tokenFactory.validateToken(it)
            val auth = tokenFactory.getAuthenticationByToken(it)
            SecurityContextHolder.getContext().authentication = auth
        }

        chain.doFilter(request, response)
    }
}