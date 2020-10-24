package com.ladys.space.api.services

import com.ladys.space.api.services.ErrorMessageService.Keys.DUPLICATE_EMAIL
import com.ladys.space.api.errors.exceptions.BadValueException
import com.ladys.space.api.repositories.UserRepository
import com.ladys.space.api.services.helpers.ConverterHelper.Companion.base64ToString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ValidationService {

    @Autowired
    private lateinit var errorMessageService: ErrorMessageService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Throws(BadValueException::class)
    fun validateEmail(email: String) {
        base64ToString(email).also {
            this.userRepository.findByEmail(it)?.let {
                throw BadValueException(this.errorMessageService.getMessage(DUPLICATE_EMAIL))
            }
        }
    }

}