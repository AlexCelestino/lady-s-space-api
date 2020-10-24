package com.ladys.space.api.security.filter

import com.ladys.space.api.constants.ErrorMessage
import com.ladys.space.api.constants.ErrorMessage.Keys.INVALID_TOKEN
import com.ladys.space.api.constants.ErrorMessage.Keys.USER_NOT_FOUND
import com.ladys.space.api.errors.exceptions.InvalidTokenException
import com.ladys.space.api.security.JwtSecurity
import com.ladys.space.api.services.UserService
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
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class RequestFilter(
        private val userService: UserService,
        private val jwtSecurity: JwtSecurity,
        @Qualifier("handlerExceptionResolver")
        private val resolver: HandlerExceptionResolver
) : OncePerRequestFilter() {

    @Autowired
    private lateinit var errorMessages: ErrorMessage

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filter: FilterChain) {
        try {
            val authorization: String? = request.getHeader(AUTHORIZATION)
            if (authorization != null && authorization.startsWith("Bearer ")) {
                val token: String = authorization.split(" ")[1]
                val userLogin: String by lazy { this.jwtSecurity.getLogin(token) }

                if (this.jwtSecurity.isTokenValid(token)) {
                    val details: UserDetails? = this.userService.loadUserByUsername(userLogin)

                    if (details == null) {
                        this.resolver.resolveException(
                                request,
                                response,
                                null,
                                UsernameNotFoundException(this.errorMessages.getMessage(USER_NOT_FOUND))
                        )
                        return
                    }

                    UsernamePasswordAuthenticationToken(details, null, details.authorities).also {
                        SecurityContextHolder.getContext().authentication = it
                    }
                }
            }
        } catch (e: Exception) {
            this.resolver.resolveException(
                    request,
                    response,
                    null,
                    InvalidTokenException(this.errorMessages.getMessage(INVALID_TOKEN))
            )
            return
        }
        filter.doFilter(request, response)
    }
}

