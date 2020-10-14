package com.ladys.space.api.services

import com.ladys.space.api.constants.ApiConstants
import com.ladys.space.api.constants.ErrorMessage
import com.ladys.space.api.constants.ErrorMessage.Keys.DUPLICATE_EMAIL
import com.ladys.space.api.errors.exceptions.BadValueException
import com.ladys.space.api.errors.exceptions.InvalidTokenException
import com.ladys.space.api.repositories.UserRepository
import com.ladys.space.api.services.helpers.ApiHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ValidationService {

    @Autowired
    private lateinit var jwtService: JwtService

    @Autowired
    private lateinit var errorMessage: ErrorMessage

    @Autowired
    private lateinit var apiHelper: ApiHelper

    @Autowired
    private lateinit var userRepository: UserRepository

    @Throws(InvalidTokenException::class)
    fun validateToken(customToken: String?, locale: Locale) {
        customToken?.let { token ->
            if (!token.startsWith(ApiConstants.Headers.OCCURRENCES_HEADER))
                throw InvalidTokenException(this.errorMessage.getMessage(ErrorMessage.Keys.INVALID_TOKEN, locale))

            token.split(" ")[1].also {
                if (!this.jwtService.isTokenValid(it, locale))
                    throw InvalidTokenException(this.errorMessage.getMessage(ErrorMessage.Keys.INVALID_TOKEN, locale))
            }
        } ?: throw InvalidTokenException(this.errorMessage.getMessage(ErrorMessage.Keys.INVALID_TOKEN, locale))
    }

    @Throws(BadValueException::class)
    fun validateEmail(email: String, locale: Locale) {
        this.apiHelper.base64ToString(email).also {
            this.userRepository.findByEmail(it)?.let {
                throw BadValueException(this.errorMessage.getMessage(DUPLICATE_EMAIL, locale))
            }
        }
    }

}