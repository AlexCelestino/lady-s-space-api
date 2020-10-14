package com.brazil.area.codes.api.services

import com.brazil.area.codes.api.errors.ErrorMessages.Companion.AREA_CODE_NOT_FOUND
import com.brazil.area.codes.api.errors.exceptions.ResourceNotFoundException
import com.brazil.area.codes.api.models.AreaCodeModel
import com.brazil.area.codes.api.repositories.AreaCodeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AreaCodeService {

    @Autowired
    private lateinit var areaCodeRepository: AreaCodeRepository

    fun findByAreaCode(areaCode: String): AreaCodeModel =
            this.areaCodeRepository.findByAreaCode(areaCode) ?: throw ResourceNotFoundException(AREA_CODE_NOT_FOUND)

}