package com.brazil.area.codes.api.controllers

import com.brazil.area.codes.api.models.StateModel
import com.brazil.area.codes.api.models.dto.StateDTO
import com.brazil.area.codes.api.services.StateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/state")
class StateController {

    @Autowired
    private lateinit var stateService: StateService

    @GetMapping("{federativeUnit}")
    fun getByState(@PathVariable("federativeUnit") federativeUnit: String): ResponseEntity<StateDTO> =
            ok(this.stateService.findByCode(federativeUnit))

    @GetMapping
    fun getAllStates(): ResponseEntity<List<StateModel>> = ok(this.stateService.findAll())

}