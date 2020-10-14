package com.brazil.area.codes.api.services

import com.brazil.area.codes.api.errors.ErrorMessages.Companion.STATE_NOT_FOUND
import com.brazil.area.codes.api.errors.exceptions.ResourceNotFoundException
import com.brazil.area.codes.api.models.StateModel
import com.brazil.area.codes.api.models.dto.StateDTO
import com.brazil.area.codes.api.repositories.StateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StateService {

    @Autowired
    private lateinit var stateRepository: StateRepository

    fun findByCode(federativeUnit: String): StateDTO = this.modelToDTO(federativeUnit)

    fun findAll(): List<StateModel> = this.stateRepository.findAll().toList()

    private fun modelToDTO(federativeUnit: String): StateDTO =
            this.stateRepository.findByFederativeUnit(federativeUnit.toUpperCase())?.run {
                val areaCodes: MutableList<String> = mutableListOf()

                StateDTO(
                        state = this.state,
                        federativeUnit = this.federativeUnit,
                        areaCodes = this.areaCodes!!.run {
                            forEach { areaCodes.add(it.areaCode) }
                            areaCodes
                        }
                )

            } ?: throw ResourceNotFoundException(STATE_NOT_FOUND)

}