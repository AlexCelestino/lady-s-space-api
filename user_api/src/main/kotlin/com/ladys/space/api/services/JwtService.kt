package com.ladys.space.api.services

import com.ladys.space.api.constants.ErrorMessage
import com.ladys.space.api.constants.ErrorMessage.Keys.INVALID_TOKEN
import com.ladys.space.api.errors.exceptions.InvalidTokenException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.SignatureException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Service
class JwtService {

    @Value("%{jwt.secret}")
    private lateinit var secretKey: String

    @Value("%{jwt.expiration}")
    private lateinit var expiration: String

    @Autowired
    private lateinit var errorMessage: ErrorMessage

    fun generateToken(email: String): String = Jwts.builder()
            .setSubject(email)
            .setExpiration(this.expireDate())
            .signWith(SignatureAlgorithm.HS512, this.secretKey)
            .compact()

    fun isTokenValid(token: String, locale: Locale? = null): Boolean {
        val claims: Claims = this.getClaims(token, locale)
        val expirationDate: Date = claims.expiration

        val expirationDateTime: LocalDateTime =
                expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

        if (!LocalDateTime.now().isAfter(expirationDateTime))
            return true

        return false
    }

    fun getLogin(token: String): String = this.getClaims(token).subject

    fun expireDate(): Date = Date
            .from(LocalDateTime.now().plusHours(this.expiration.toLong()).atZone(ZoneId.systemDefault()).toInstant())

    private fun getClaims(token: String, locale: Locale? = null): Claims =
            try {
                Jwts.parser()
                        .setSigningKey(this.secretKey)
                        .parseClaimsJws(token)
                        .body
            } catch (e: SignatureException) {
                throw InvalidTokenException(this.errorMessage.getMessage(INVALID_TOKEN, locale!!))
            }

}