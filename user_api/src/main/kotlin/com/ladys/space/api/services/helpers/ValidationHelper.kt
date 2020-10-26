package com.ladys.space.api.services.helpers

import com.ladys.space.api.services.ErrorMessageService
import com.ladys.space.api.errors.exceptions.BadValueException
import com.ladys.space.api.repositories.UserRepository
import com.ladys.space.api.security.JwtSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.LocalDate
import java.time.Period

class ValidationHelper(
        private val userRepository: UserRepository,
        private val errorMessagesService: ErrorMessageService,
        private val encoder: BCryptPasswordEncoder,
        private val jwtSecurity: JwtSecurity
) {

    @Throws(BadValueException::class)
    fun verifyDuplicity(email: String): String = this.userRepository.findByEmail(email)?.let {
        throw BadValueException(this.errorMessagesService.getMessage(ErrorMessageService.Keys.DUPLICATE_EMAIL))
    } ?: email

    @Throws(BadValueException::class)
    fun validateMinimumAge(age: LocalDate): LocalDate =
            Period.between(LocalDate.of(age.year, age.month, age.dayOfMonth), LocalDate.now()).years.run {
                if (this < 14)
                    throw BadValueException(errorMessagesService.getMessage(ErrorMessageService.Keys.INVALID_AGE))
                else
                    age
            }

    fun isPasswordsEquals(password: String, toCompare: String): Boolean = (encoder.matches(password, toCompare))


}