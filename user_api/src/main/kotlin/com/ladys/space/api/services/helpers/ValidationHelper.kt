package com.ladys.space.api.services.helpers

import com.ladys.space.api.constants.ErrorMessage
import com.ladys.space.api.errors.exceptions.BadValueException
import com.ladys.space.api.repositories.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.LocalDate
import java.time.Period

class ValidationHelper(
        private val userRepository: UserRepository,
        private val errorMessages: ErrorMessage,
        private val encoder: BCryptPasswordEncoder
) {

    @Throws(BadValueException::class)
    fun verifyDuplicity(email: String): String = this.userRepository.findByEmail(email)?.let {
        throw BadValueException(this.errorMessages.getMessage(ErrorMessage.Keys.DUPLICATE_EMAIL))
    } ?: email

    @Throws(BadValueException::class)
    fun validateMinimumAge(age: LocalDate): LocalDate {
        Period.between(LocalDate.of(age.year, age.month, age.dayOfMonth), LocalDate.now()).years.also {
            if (it < 14)
                throw BadValueException(this.errorMessages.getMessage(ErrorMessage.Keys.INVALID_AGE))
            else
                return age
        }
    }

    fun isPasswordsEquals(password: String, toCompare: String): Boolean = (encoder.matches(password, toCompare))

}