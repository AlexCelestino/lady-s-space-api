package com.ladys.space.occurrences.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service

@Service
class ErrorMessageService {

    @Autowired
    private lateinit var messageSource: MessageSource

    fun message(key: String): String {
        LocaleContextHolder.getLocale().also {
            return this.messageSource.getMessage(key, null, it)
        }
    }

    object Keys {
        const val INVALID_TOKEN: String = "invalid-token"
        const val USER_UNAUTHORIZED: String = "user-unauthorized"
        const val MISSING_AUTHORIZATION_HEADER: String = "missing-authorization-header"
        const val CONNECTION_ERROR: String = "connection-error"
        const val REGION_NOT_FOUND: String = "region-not-found"
        const val CITY_NOT_FOUND: String = "city-not-found"
        const val ZONE_NOT_FOUND: String = "zone-not-found"
        const val OCCURRENCE_NOT_FOUND: String = "occurrence-not-found"
    }
}