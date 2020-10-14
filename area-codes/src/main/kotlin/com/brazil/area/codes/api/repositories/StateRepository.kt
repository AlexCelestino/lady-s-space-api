package com.brazil.area.codes.api.repositories

import com.brazil.area.codes.api.models.StateModel
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface StateRepository : CrudRepository<StateModel, Int> {

    fun findByFederativeUnit(@Param("federativeUnit") federativeUnit: String): StateModel?

}