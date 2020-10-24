package com.ladys.space.api.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service

@Service
class ErrorMessageService {

    @Autowired
    private lateinit var messageSource: MessageSource

    fun getMessage(key: String): String {
        LocaleContextHolder.getLocale().also {
            return this.messageSource.getMessage(key, null, it)
        }
    }

    object Keys {
        const val DUPLICATE_EMAIL: String = "user.duplicate.email"
        const val INVALID_AGE: String = "user.invalid.age"
        const val USER_NOT_FOUND: String = "user.user.not.found"
        const val INCORRECT_LOGIN: String = "login.incorrect.login"
        const val INVALID_TOKEN: String = "invalid.token"
        const val EQUAL_PASSWORDS: String = "password.equal.passwords"
    }
}