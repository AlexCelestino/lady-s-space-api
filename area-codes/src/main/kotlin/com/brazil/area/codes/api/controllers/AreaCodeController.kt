package com.brazil.area.codes.api.controllers

import com.brazil.area.codes.api.models.AreaCodeModel
import com.brazil.area.codes.api.services.AreaCodeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@RequestMapping("/area-code")
class AreaCodeController {

    @Autowired
    private lateinit var areaCodeService: AreaCodeService

    @GetMapping("{areaCode}")
    fun getByAreaCode(@PathVariable("areaCode") areaCode: String): ResponseEntity<AreaCodeModel> =
            ResponseEntity.ok(this.areaCodeService.findByAreaCode(areaCode))

}