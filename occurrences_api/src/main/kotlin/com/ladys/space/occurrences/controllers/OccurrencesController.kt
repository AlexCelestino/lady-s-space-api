package com.ladys.space.occurrences.controllers

import com.ladys.space.occurrences.constants.ApiConstants.Api.KEY
import com.ladys.space.occurrences.constants.ApiConstants.Api.MEDIA_TYPE
import com.ladys.space.occurrences.constants.ApiConstants.Parameters.Occurrences.CRITERIA
import com.ladys.space.occurrences.constants.ApiConstants.Parameters.Occurrences.CRITERIA_DESCRIPTION
import com.ladys.space.occurrences.constants.ApiConstants.Parameters.Ranking.FILTER
import com.ladys.space.occurrences.constants.ApiConstants.Parameters.Ranking.FILTER_DESCRIPTION
import com.ladys.space.occurrences.constants.ApiConstants.Parameters.Ranking.ZONE
import com.ladys.space.occurrences.constants.ApiConstants.Parameters.Ranking.ZONE_DESCRIPTION
import com.ladys.space.occurrences.errors.ApiErrors
import com.ladys.space.occurrences.models.OccurrencesModel
import com.ladys.space.occurrences.services.OccurrencesService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/occurrences")
class OccurrencesController {

    @Autowired
    private lateinit var mService: OccurrencesService

    @Operation(
            method = "GET",
            summary = "All occurrences.",
            description = "Returns a list with the occurrences.",
            security = [SecurityRequirement(name = KEY)],
            parameters = [
                Parameter(name = FILTER, description = FILTER_DESCRIPTION),
                Parameter(name = ZONE, description = ZONE_DESCRIPTION)
            ],
            responses = [
                ApiResponse(
                        responseCode = "200",
                        description = "OK.",
                        content = [
                            Content(mediaType = MEDIA_TYPE, schema = Schema(implementation = OccurrencesModel::class))
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
    @GetMapping
    fun getAll(
            @RequestParam("filter") filter: String?,
            @RequestParam("zone", required = false) zone: String?,
            @RequestHeader(name = AUTHORIZATION, required = false) token: String?
    ): ResponseEntity<List<OccurrencesModel>> =
            this.mService.getAllOccurrences(filter, zone, token)?.run { ok().body(this) }
                    ?: noContent().build()

    @Operation(
            method = "GET",
            summary = "Specific occurrence.",
            description = "Returns a specific occurrence by criteria.",
            security = [SecurityRequirement(name = KEY)],
            parameters = [Parameter(name = CRITERIA, description = CRITERIA_DESCRIPTION)],
            responses = [
                ApiResponse(
                        responseCode = "200",
                        description = "OK.",
                        content = [
                            Content(mediaType = MEDIA_TYPE, schema = Schema(implementation = OccurrencesModel::class))
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
            ])
    @GetMapping("/{criteria}")
    fun getOneOccurrence(
            @PathVariable(name = "criteria", required = true) criteria: String,
            @RequestHeader(name = AUTHORIZATION, required = false) token: String?): ResponseEntity<OccurrencesModel> =
            ok(this.mService.getOneOccurrence(token, criteria))

}