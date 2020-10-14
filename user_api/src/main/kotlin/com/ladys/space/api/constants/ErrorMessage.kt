package com.ladys.space.api.constants

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import java.util.*

@Component
class ErrorMessage {

    @Autowired
    private lateinit var messageSource: MessageSource

    fun getMessage(key: String, locale: Locale): String =
            this.messageSource.getMessage(key, null, locale)

    object Keys {
        const val NULL_FIELDS: String = "null.fields"

        const val NULL_USER: String = "register.null.user"

        const val NULL_ADDRESS: String = "register.null.address"

        const val NULL_CONTACT: String = "register.null.contact"

        const val DUPLICATE_EMAIL: String = "user.duplicate.email"

        const val INVALID_EMAIL: String = "user.invalid.email"

        const val INVALID_PASSWORD_LENGTH: String = "user.invalid.password.length"

        const val INVALID_AGE: String = "user.invalid.age"

        const val USER_NOT_FOUND: String = "user.user.not.found"

        const val INVALID_PHONE_NUMBER: String = "invalid.phone.number"

        const val INVALID_AREA_CODE: String = "invalid.area.code"

        const val INVALID_EMERGENCY_CONTACT: String = "emergency.contact.invalid.contacts"

        const val INVALID_MAX_EMERGENCY_CONTACTS: String = "emergency.contact.invalid.max.contacts"

        const val INVALID_ZIP_CODE: String = "address.invalid.zip.code"

        const val INCORRECT_LOGIN: String = "login.incorrect.login"

        const val INCORRECT_PASSWORD: String = "login.incorrect.password"

        const val INVALID_TOKEN: String = "invalid.token"
    }
}