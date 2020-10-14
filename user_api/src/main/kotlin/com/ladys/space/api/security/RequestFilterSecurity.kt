package com.ladys.space.api.security

import com.ladys.space.api.constants.ErrorMessage
import com.ladys.space.api.constants.ErrorMessage.Keys.INVALID_TOKEN
import com.ladys.space.api.constants.ErrorMessage.Keys.USER_NOT_FOUND
import com.ladys.space.api.errors.exceptions.InvalidTokenException
import com.ladys.space.api.services.JwtService
import com.ladys.space.api.services.LoginService
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.SignatureException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class RequestFilterSecurity(
        private val loginService: LoginService,
        private val jwtService: JwtService,
        @Qualifier("handlerExceptionResolver")
        private val resolver: HandlerExceptionResolver
) : OncePerRequestFilter() {

    @Autowired
    private lateinit var errorMessages: ErrorMessage

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filter: FilterChain) {
        val locale: Locale = request.locale
        val authorization: String? = request.getHeader(AUTHORIZATION)

        try {
            if (authorization != null && authorization.startsWith("Bearer ")) {
                val token: String = authorization.split(" ")[1]
                val userLogin: String by lazy { this.jwtService.getLogin(token) }

                if (this.jwtService.isTokenValid(token)) {
                    val details: UserDetails? = this.loginService.loadUserByUsername(userLogin)

                    if (details == null) {
                        this.resolver.resolveException(
                                request,
                                response,
                                null,
                                UsernameNotFoundException(this.errorMessages.getMessage(USER_NOT_FOUND, locale))
                        )
                        return
                    }

                    val authenticationToken =
                            UsernamePasswordAuthenticationToken(details, null, details.authorities)

                    SecurityContextHolder.getContext().authentication = authenticationToken
                }
            }
        } catch (e: ExpiredJwtException) {
            this.resolver.resolveException(
                    request,
                    response,
                    null,
                    InvalidTokenException(this.errorMessages.getMessage(INVALID_TOKEN, locale))
            )
            return
        } catch (e: SignatureException) {
            this.resolver.resolveException(
                    request,
                    response,
                    null,
                    InvalidTokenException(this.errorMessages.getMessage(INVALID_TOKEN, locale))
            )
            return
        }

        filter.doFilter(request, response)
    }
}

