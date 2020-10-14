package com.ladys.space.api.services.helpers

import com.ladys.space.api.constants.ErrorMessage
import com.ladys.space.api.constants.ErrorMessage.Keys.INVALID_AGE
import com.ladys.space.api.constants.ErrorMessage.Keys.INVALID_AREA_CODE
import com.ladys.space.api.constants.ErrorMessage.Keys.INVALID_EMAIL
import com.ladys.space.api.constants.ErrorMessage.Keys.INVALID_PASSWORD_LENGTH
import com.ladys.space.api.constants.ErrorMessage.Keys.INVALID_PHONE_NUMBER
import com.ladys.space.api.constants.ErrorMessage.Keys.INVALID_ZIP_CODE
import com.ladys.space.api.constants.ErrorMessage.Keys.NULL_FIELDS
import com.ladys.space.api.errors.exceptions.BadValueException
import com.ladys.space.api.models.dto.AddressDTO
import com.ladys.space.api.models.dto.EmergencyContactDTO
import com.ladys.space.api.models.dto.PersonalContactDTO
import com.ladys.space.api.models.dto.UserDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.Period
import java.util.*
import java.util.regex.Pattern
import kotlin.reflect.full.memberProperties

@Service
@PropertySource("classpath:error-messages.properties")
class ApiHelper private constructor() {

    @Autowired
    private lateinit var errorMessages: ErrorMessage

    @Throws(BadValueException::class)
    fun validatePhoneNumber(phoneNumber: String, locale: Locale) {
        if (phoneNumber.length < 9 || !phoneNumber.startsWith("9"))
            throw BadValueException(this.errorMessages.getMessage(INVALID_PHONE_NUMBER, locale))
    }

    @Throws(BadValueException::class)
    fun verifyAreaCode(areaCode: String, locale: Locale) {
        if (areaCode.length < 2)
            throw BadValueException(this.errorMessages.getMessage(INVALID_AREA_CODE, locale))
    }

    @Throws(BadValueException::class)
    fun verifyPasswordLength(password: String, locale: Locale) {
        if (password.length < 6) throw BadValueException(this.errorMessages.getMessage(INVALID_PASSWORD_LENGTH, locale))
    }

    @Throws(BadValueException::class)
    fun validateEmail(email: String, locale: Locale) {
        if (!Pattern.compile("^(.+)@(.+)$").matcher(email).matches())
            throw BadValueException(this.errorMessages.getMessage(INVALID_EMAIL, locale))
    }

    @Throws(BadValueException::class)
    fun validateZipCode(zipCode: String, locale: Locale) {
        if (zipCode.length < 8) throw BadValueException(this.errorMessages.getMessage(INVALID_ZIP_CODE, locale))
    }

    @Throws(BadValueException::class)
    fun validateMinimumAge(age: LocalDate, locale: Locale): LocalDate {
        val calculatedAge: Int = Period.between(
                LocalDate.of(age.year, age.month, age.dayOfMonth),
                LocalDate.now()
        ).years

        if (calculatedAge < 14)
            throw BadValueException(this.errorMessages.getMessage(INVALID_AGE, locale)) else return age
    }

    @Throws(BadValueException::class)
    fun verifyNullFields(instance: UserDTO, locale: Locale) {
        val hasErrors: Boolean = UserDTO::class.memberProperties.any {
            it.get(instance) == "" || it.get(instance) == null
        }
        if (hasErrors) throw BadValueException(this.errorMessages.getMessage(NULL_FIELDS, locale))
    }

    @Throws(BadValueException::class)
    fun verifyNullFields(instance: AddressDTO, locale: Locale) {
        val hasErrors: Boolean = AddressDTO::class.memberProperties.any {
            it.get(instance) == "" || it.get(instance) == null
        }

        if (hasErrors) throw BadValueException(this.errorMessages.getMessage(NULL_FIELDS, locale))
    }

    @Throws(BadValueException::class)
    fun verifyNullFields(instance: PersonalContactDTO, locale: Locale) {
        val hasErrors: Boolean = PersonalContactDTO::class.memberProperties.any {
            it.get(instance) == "" || it.get(instance) == null
        }

        if (hasErrors) throw BadValueException(this.errorMessages.getMessage(NULL_FIELDS, locale))
    }

    @Throws(BadValueException::class)
    fun verifyNullFields(instance: EmergencyContactDTO, locale: Locale) {
        val hasErrors: Boolean = EmergencyContactDTO::class.memberProperties.any {
            it.get(instance) == "" || it.get(instance) == null
        }

        if (hasErrors) throw BadValueException(this.errorMessages.getMessage(NULL_FIELDS, locale))
    }

    fun toBase64(value: String): String = Base64.getEncoder().encodeToString(value.toByteArray())

    fun base64ToString(value: String): String = String(Base64.getDecoder().decode(value))

}