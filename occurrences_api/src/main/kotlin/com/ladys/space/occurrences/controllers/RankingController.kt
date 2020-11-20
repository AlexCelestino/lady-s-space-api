package com.ladys.space.occurrences.controllers

import com.ladys.space.occurrences.constants.ApiConstants.Api.KEY
import com.ladys.space.occurrences.constants.ApiConstants.Api.MEDIA_TYPE
import com.ladys.space.occurrences.constants.ApiConstants.Parameters.Ranking.FILTER
import com.ladys.space.occurrences.constants.ApiConstants.Parameters.Ranking.FILTER_DESCRIPTION
import com.ladys.space.occurrences.constants.ApiConstants.Parameters.Ranking.ZONE
import com.ladys.space.occurrences.constants.ApiConstants.Parameters.Ranking.ZONE_DESCRIPTION
import com.ladys.space.occurrences.errors.ApiErrors
import com.ladys.space.occurrences.models.RankingModel
import com.ladys.space.occurrences.services.RankingService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class RankingController {

    @Autowired
    private lateinit var mService: RankingService

    @Operation(
            method = "GET",
            summary = "Occurrences ranking.",
            description = "Returns a ranking with the highest occurrences.",
            security = [SecurityRequirement(name = KEY)],
            parameters = [
                Parameter(name = FILTER, `in` = QUERY, description = FILTER_DESCRIPTION),
                Parameter(name = ZONE, `in` = QUERY, description = ZONE_DESCRIPTION)
            ],
            responses = [
                ApiResponse(
                        responseCode = "200",
                        description = "OK.",
                        content = [
                            Content(mediaType = MEDIA_TYPE, schema = Schema(implementation = RankingModel::class))
                        ]
                ),
                ApiResponse(
                        responseCode = "204",
                        description = "No content.",
                        content = [
                            Content(mediaType = MEDIA_TYPE)
                        ]
                ),
                ApiResponse(
                        responseCode = "403",
                        description = "Authorization header invalid or expired.",
                        content = [
                            Content(mediaType = MEDIA_TYPE, schema = Schema(implementation = ApiErrors::class))
                        ]
                )
            ]
    )
    @GetMapping("/ranking")
    fun getRanking(
            @RequestHeader(name = AUTHORIZATION, required = false) token: String?,
            @RequestParam(name = "filter") filter: String?,
            @RequestParam(name = "zone", required = false) zone: String?
    ): ResponseEntity<List<RankingModel>> = this.mService.getRanking(token, filter, zone)?.run {
        ok().body(this)
    } ?: noContent().build()

}