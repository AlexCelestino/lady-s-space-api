package com.ladys.space.occurrences.services

import com.ladys.space.occurrences.enums.RankingFilterEnum.*
import com.ladys.space.occurrences.errors.exceptions.ResourceNotFoundException
import com.ladys.space.occurrences.models.OccurrencesModel
import com.ladys.space.occurrences.repositories.OccurrencesRepository
import com.ladys.space.occurrences.services.ErrorMessageService.Keys.OCCURRENCE_NOT_FOUND
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class OccurrencesService {

    @Autowired
    private lateinit var mRepository: OccurrencesRepository

    @Autowired
    private lateinit var mMessage: ErrorMessageService

    private val validationTokenHelper: ValidationTokenHelper by lazy {
        ValidationTokenHelper(this.mMessage)
    }

    fun getAllOccurrences(filter: String?, zone: String?, token: String?): List<OccurrencesModel>? {
        this.validationTokenHelper.validateToken(token)

        return filter?.let {
            when (filter) {
                CAPITAL.filter -> if (zone != null)
                    this.mRepository.findByRegionAndZone(zone = zone)
                else
                    this.mRepository.findByCapital()

                METROPOLITAN_REGION.filter -> this.mRepository.findAllByMetropolitanCity()

                OTHER_CITIES.filter -> this.mRepository.findAll().filter {
                    it.region != "Capital" && it.region != "Grande São Paulo (exclui a Capital)"
                }

                else -> null
            }
        } ?: this.mRepository.findAll()

    }

    fun getOneOccurrence(token: String?, name: String): OccurrencesModel {
        this.validationTokenHelper.validateToken(token)

        val byNeighbourhood: OccurrencesModel? = this.mRepository.findByNeighbourhood(name)

        val byCity: OccurrencesModel? = this.mRepository.findByCity(name)

        return when {
            byNeighbourhood != null -> byNeighbourhood

            byCity != null -> byCity

            else -> throw ResourceNotFoundException(this.mMessage.message(OCCURRENCE_NOT_FOUND))
        }

    }

}