package com.ladys.space.api.services

import com.ladys.space.api.constants.ErrorMessage
import com.ladys.space.api.constants.ErrorMessage.Keys.INCORRECT_LOGIN
import com.ladys.space.api.errors.exceptions.BadCredentialsException
import com.ladys.space.api.models.dto.LoginDTO
import com.ladys.space.api.models.dto.TokenDTO
import com.ladys.space.api.repositories.UserRepository
import com.ladys.space.api.services.helpers.ApiHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Service
class LoginService : UserDetailsService {

    @Autowired
    private lateinit var jwtService: JwtService

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var errorMessages: ErrorMessage

    @Autowired
    private lateinit var apiHelper: ApiHelper

    fun authenticateUser(loginDTO: LoginDTO, locale: Locale): TokenDTO {
        val userDetails: UserDetails by lazy {
            loadUserByUsername(loginDTO.email)
                    ?: throw BadCredentialsException(errorMessages.getMessage(INCORRECT_LOGIN, locale))
        }

        val token: String by lazy { this.jwtService.generateToken(loginDTO.email) }

        val expireDate: String by lazy {
            val date: LocalDateTime = this.jwtService.expireDate()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()

            this.apiHelper.toBase64(date.toString())
        }

        return if (isPasswordsEquals(loginDTO.password, userDetails.password))
            throw BadCredentialsException(errorMessages.getMessage(INCORRECT_LOGIN, locale))
        else
            TokenDTO(token, expireDate)
    }

    override fun loadUserByUsername(email: String): UserDetails? =
            this.userRepository.findByEmail(email)?.let {
                User.builder()
                        .username(it.email)
                        .password(it.password)
                        .roles("")
                        .build()
            }

    private fun isPasswordsEquals(password: String, toCompare: String): Boolean =
            (!passwordEncoder.matches(password, toCompare))

}