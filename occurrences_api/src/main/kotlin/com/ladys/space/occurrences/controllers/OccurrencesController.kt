package com.ladys.space.occurrences.controllers

import com.ladys.space.occurrences.models.OccurrencesDTO
import com.ladys.space.occurrences.models.dto.RankingDTO
import com.ladys.space.occurrences.services.OccurrencesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class OccurrencesController {

    @Autowired
    private lateinit var occurrencesService: OccurrencesService

    @GetMapping("/{year}")
    fun getOccurrences(
            @PathVariable("year") year: Int,
            @RequestHeader(name = AUTHORIZATION, required = true)
            token: String
    ): ResponseEntity<List<OccurrencesDTO>> =
            this.occurrencesService.getOccurrences(year, token)?.let {
                ResponseEntity.accepted().body(it.toList())
            } ?: ResponseEntity.status(HttpStatus.NO_CONTENT).build()


    @GetMapping("/ranking/{year}")
    fun getTopFive(
            @PathVariable("year")
            year: Int,
            @RequestHeader(name = AUTHORIZATION, required = true)
            token: String
    ): ResponseEntity<RankingDTO> =
            this.occurrencesService.getTopFive(year, token)?.let {
                ResponseEntity.accepted().body(it)
            } ?: ResponseEntity.status(HttpStatus.NO_CONTENT).build()

}