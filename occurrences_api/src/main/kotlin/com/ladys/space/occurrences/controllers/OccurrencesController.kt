package com.ladys.space.occurrences.controllers

import com.ladys.space.occurrences.models.dto.OccurrencesDTO
import com.ladys.space.occurrences.models.dto.RankingDTO
import com.ladys.space.occurrences.services.OccurrencesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class OccurrencesController {

    @Autowired
    private lateinit var occurrencesService: OccurrencesService

    @GetMapping("/{year}")
    fun getByRegion(
            @PathVariable("year") year: Int,
            @RequestParam("region") region: String?,
            @RequestHeader(name = AUTHORIZATION) token: String?
    ): ResponseEntity<List<OccurrencesDTO>> =
            this.occurrencesService.getOccurrences(year, region, token)?.let {
                ResponseEntity.accepted().body(it)
            } ?: ResponseEntity.status(HttpStatus.NO_CONTENT).build()

    @GetMapping("/ranking/{year}")
    fun getTopFive(
            @PathVariable("year") year: Int,
            @RequestHeader(name = AUTHORIZATION) token: String?
    ): ResponseEntity<RankingDTO> =
            this.occurrencesService.getTopFive(year, token)?.let {
                ResponseEntity.accepted().body(it)
            } ?: ResponseEntity.status(HttpStatus.NO_CONTENT).build()

}