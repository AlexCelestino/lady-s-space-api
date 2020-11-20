package com.ladys.space.occurrences.services

import com.ladys.space.occurrences.constants.ApiConstants.Url.USER_API
import com.ladys.space.occurrences.errors.exceptions.ClientErrorException
import com.ladys.space.occurrences.errors.exceptions.InvalidAccessException
import com.ladys.space.occurrences.services.ErrorMessageService.Keys.CONNECTION_ERROR
import com.ladys.space.occurrences.services.ErrorMessageService.Keys.INVALID_TOKEN
import com.ladys.space.occurrences.services.ErrorMessageService.Keys.MISSING_AUTHORIZATION_HEADER
import com.ladys.space.occurrences.services.ErrorMessageService.Keys.USER_UNAUTHORIZED
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpMethod.GET
import org.springframework.web.client.HttpClientErrorException.Forbidden
import org.springframework.web.client.HttpClientErrorException.Unauthorized
import org.springframework.web.client.RestTemplate
import java.net.ConnectException
import java.net.URI

class ValidationTokenHelper(private val errorMessageService: ErrorMessageService) {

    private val restTemplate: RestTemplate = RestTemplate()

    private val httpHeaders: HttpHeaders = HttpHeaders()

    fun validateToken(token: String?) {
        var adjustedToken: String

        token?.let {
            if (it.contains("Bearer")) {
                adjustedToken = it.replace("Bearer ", "occurrencesToken ")
                this.httpHeaders.apply { this.set(AUTHORIZATION, adjustedToken) }
            } else {
                this.httpHeaders.apply { this.set(AUTHORIZATION, it) }
            }

            val requestEntity: HttpEntity<String> = HttpEntity(null, httpHeaders)

            val uri = URI(USER_API)

            try {
                this.restTemplate.exchange(uri, GET, requestEntity, String::class.java)
            } catch (e: ConnectException) {
                throw ConnectException(this.errorMessageService.message(CONNECTION_ERROR))
            } catch (e: Unauthorized) {
                throw ClientErrorException(this.errorMessageService.message(USER_UNAUTHORIZED))
            } catch (e: Forbidden) {
                throw InvalidAccessException(this.errorMessageService.message(INVALID_TOKEN))
            }

        } ?: throw InvalidAccessException(errorMessageService.message(MISSING_AUTHORIZATION_HEADER))

    }

}