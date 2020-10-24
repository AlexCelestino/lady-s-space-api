package com.ladys.space.api.services

import com.ladys.space.api.constants.ApiConstants.Headers.OCCURRENCES_HEADER
import com.ladys.space.api.constants.ErrorMessage
import com.ladys.space.api.constants.ErrorMessage.Keys.DUPLICATE_EMAIL
import com.ladys.space.api.constants.ErrorMessage.Keys.INVALID_TOKEN
import com.ladys.space.api.errors.exceptions.BadValueException
import com.ladys.space.api.errors.exceptions.InvalidTokenException
import com.ladys.space.api.repositories.UserRepository
import com.ladys.space.api.services.helpers.ConverterHelper.Companion.base64ToString
import com.ladys.space.api.security.JwtSecurity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ValidationService {

    @Autowired
    private lateinit var jwtSecurity: JwtSecurity

    @Autowired
    private lateinit var errorMessage: ErrorMessage

    @Autowired
    private lateinit var userRepository: UserRepository

    @Throws(InvalidTokenException::class)
    fun validateToken(customToken: String?) {
        customToken?.let { token ->
            if (!token.startsWith(OCCURRENCES_HEADER))
                throw InvalidTokenException(this.errorMessage.getMessage(INVALID_TOKEN))

            token.split(" ")[1].also {
                if (!this.jwtSecurity.isTokenValid(it))
                    throw InvalidTokenException(this.errorMessage.getMessage(INVALID_TOKEN))
            }
        } ?: throw InvalidTokenException(this.errorMessage.getMessage(INVALID_TOKEN))
    }

    @Throws(BadValueException::class)
    fun validateEmail(email: String) {
        base64ToString(email).also {
            this.userRepository.findByEmail(it)?.let {
                throw BadValueException(this.errorMessage.getMessage(DUPLICATE_EMAIL))
            }
        }
    }

}