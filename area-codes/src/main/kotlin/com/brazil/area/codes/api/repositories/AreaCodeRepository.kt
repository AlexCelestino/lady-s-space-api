package com.brazil.area.codes.api.repositories

import com.brazil.area.codes.api.models.AreaCodeModel
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface AreaCodeRepository : CrudRepository<AreaCodeModel, Int> {

    fun findByAreaCode(@Param("areaCode") areaCode: String): AreaCodeModel?

}