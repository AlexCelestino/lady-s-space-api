package com.ladys.space.occurrences.controllers

import com.ladys.space.occurrences.constants.ApiConstants.Api.KEY
import com.ladys.space.occurrences.constants.ApiConstants.Api.MEDIA_TYPE
import com.ladys.space.occurrences.constants.ApiConstants.Parameters.AllOccurrences
import com.ladys.space.occurrences.constants.ApiConstants.Parameters.Ranking
import com.ladys.space.occurrences.errors.ApiErrors
import com.ladys.space.occurrences.models.dto.OccurrencesDTO
import com.ladys.space.occurrences.models.dto.RankingDTO
import com.ladys.space.occurrences.services.OccurrencesService
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
class OccurrencesController {

    @Autowired
    private lateinit var occurrencesService: OccurrencesService

    @Operation(
            method = "GET",
            summary = "All occurrences.",
            description = "Returns a list with the occurrences.",
            security = [SecurityRequirement(name = KEY)],
            parameters = [
                Parameter(
                        name = AllOccurrences.NEIGHBOURHOOD,
                        `in` = QUERY,
                        description = AllOccurrences.NEIGHBOURHOOD_DESCRIPTION
                ),
                Parameter(
                        name = AllOccurrences.CITY,
                        `in` = QUERY,
                        description = AllOccurrences.CITY_DESCRIPTION
                ),
                Parameter(
                        name = AllOccurrences.ZONE,
                        `in` = QUERY,
                        description = AllOccurrences.ZONE_DESCRIPTION
                )
            ],
            responses = [
                ApiResponse(
                        responseCode = "200",
                        description = "OK.",
                        content = [
                            Content(mediaType = MEDIA_TYPE, schema = Schema(implementation = OccurrencesDTO::class))
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
                ),
                ApiResponse(responseCode = "404", description = "Resource not found.", content = [
                    Content(mediaType = MEDIA_TYPE, schema = Schema(implementation = ApiErrors::class))
                ])
            ]
    )
    @GetMapping("/occurrences")
    fun getAll(
            @RequestParam("neighbourhood", required = false) neighbourhood: String?,
            @RequestParam("city", required = false) city: String?,
            @RequestParam("zone", required = false) zone: String?,
            @RequestHeader(name = AUTHORIZATION, required = false) token: String?
    ): ResponseEntity<List<OccurrencesDTO>> =
            this.occurrencesService.getAllOccurrences(neighbourhood, city, zone, token)?.run { ok().body(this) }
                    ?: noContent().build()

    @Operation(
            method = "GET",
            summary = "Occurrences ranking.",
            description = "Returns a ranking with the highest occurrences.",
            security = [SecurityRequirement(name = KEY)],
            parameters = [
                Parameter(
                        name = Ranking.CAPITAL,
                        `in` = QUERY,
                        description = Ranking.CAPITAL_DESCRIPTION
                ),
                Parameter(
                        name = Ranking.METROPOLITAN_CITY,
                        `in` = QUERY,
                        description = Ranking.METROPOLITAN_CITY_DESCRIPTION
                ),
                Parameter(
                        name = Ranking.CITIES,
                        `in` = QUERY,
                        description = Ranking.CITIES_DESCRIPTION
                ),
                Parameter(
                        name = Ranking.ZONE,
                        `in` = QUERY,
                        description = Ranking.ZONE_DESCRIPTION
                )
            ],
            responses = [
                ApiResponse(
                        responseCode = "200",
                        description = "OK.",
                        content = [
                            Content(mediaType = MEDIA_TYPE, schema = Schema(implementation = RankingDTO::class))
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
            @RequestParam("capital", required = false) capital: Boolean?,
            @RequestParam("metropolitanCity", required = false) metropolitanCity: Boolean?,
            @RequestParam("cities", required = false) cities: Boolean?,
            @RequestParam("zone", required = false) zone: String?
    ) =
            this.occurrencesService.getRanking(token, capital, metropolitanCity, cities, zone)?.run {
                ok().body(this)
            } ?: noContent().build()

}