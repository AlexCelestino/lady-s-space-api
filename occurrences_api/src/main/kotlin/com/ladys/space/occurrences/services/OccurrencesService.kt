package com.ladys.space.occurrences.services

import com.ladys.space.occurrences.errors.exceptions.InvalidAccessException
import com.ladys.space.occurrences.errors.exceptions.ResourceNotFoundException
import com.ladys.space.occurrences.models.RankingModel
import com.ladys.space.occurrences.models.dto.OccurrencesDTO
import com.ladys.space.occurrences.models.dto.RankingDTO
import com.ladys.space.occurrences.repositories.OccurrencesRepository
import com.ladys.space.occurrences.services.ErrorMessageService.Keys.CITY_NOT_FOUND
import com.ladys.space.occurrences.services.ErrorMessageService.Keys.MISSING_AUTHORIZATION_HEADER
import com.ladys.space.occurrences.services.ErrorMessageService.Keys.REGION_NOT_FOUND
import com.ladys.space.occurrences.services.ErrorMessageService.Keys.ZONE_NOT_FOUND
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.stereotype.Service
import java.util.stream.Collectors


@Service
class OccurrencesService {

    @Autowired
    private lateinit var repository: OccurrencesRepository

    @Autowired
    private lateinit var errorMessageService: ErrorMessageService

    private val validationTokenHelper: ValidationTokenHelper by lazy {
        ValidationTokenHelper(this.errorMessageService)
    }

    private val sortedByTotal: Sort = Sort.by(DESC, "records.total")

    private val sortedByUpdatedAt: Sort = Sort.by(DESC, "updatedAt")

    fun getAllOccurrences(neighbourhood: String?, city: String?, zone: String?, token: String?): List<OccurrencesDTO>? {
        this.validateToken(token)

        return when {
            neighbourhood != null -> this.repository.findByNeighbourhoodOrderByUpdatedAtAsc(neighbourhood)?.let {
                listOf(OccurrencesDTO(it.region, it.city, it.neighbourhood, it.zone, it.records, it.updatedAt))
            } ?: throw ResourceNotFoundException(this.errorMessageService.getMessage(REGION_NOT_FOUND))

            city != null -> this.repository.findByCityOrderByUpdatedAtAsc(city)?.let {
                listOf(OccurrencesDTO(it.region, it.city, it.neighbourhood, it.zone, it.records, it.updatedAt))
            } ?: throw ResourceNotFoundException(this.errorMessageService.getMessage(CITY_NOT_FOUND))

            zone != null -> this.repository.findByZoneOrderByUpdatedAtAsc(zone)?.stream()?.map {
                OccurrencesDTO(it.region, it.city, it.neighbourhood, it.zone, it.records, it.updatedAt)
            }?.collect(Collectors.toList())
                    ?: throw ResourceNotFoundException(this.errorMessageService.getMessage(ZONE_NOT_FOUND))

            else -> {
                this.repository.findAll(this.sortedByUpdatedAt).stream().map {
                    OccurrencesDTO(it.region, it.city, it.neighbourhood, it.zone, it.records, it.updatedAt)
                }.collect(Collectors.toList())
            }
        }
    }

    fun getRanking(
            token: String?,
            capital: Boolean?,
            metropolitanCity: Boolean?,
            cities: Boolean?,
            zone: String?
    ): RankingDTO? {
        this.validateToken(token)

        val rankingList: MutableList<RankingModel> = mutableListOf()

        val ranking: RankingDTO by lazy {
            RankingDTO(rankingList[0], rankingList[1], rankingList[2], rankingList[3], rankingList[4])
        }

        return when {
            capital != null && capital -> {
                this.repository.findByRegion(this.pagination(this.sortedByTotal)).content.forEach {
                    rankingList.add(RankingModel(it.neighbourhood!!, it.records.total))
                }
                ranking
            }

            metropolitanCity != null && metropolitanCity -> {
                this.repository.findByMetropolitanCity(this.pagination(this.sortedByTotal)).content.forEach {
                    rankingList.add(RankingModel(it.city, it.records.total))
                }
                ranking
            }

            cities != null && cities -> {
                this.repository.findAll(this.sortedByTotal).forEach {
                    if (it.region != "Capital" && it.region != "Grande São Paulo (exclui a Capital)")
                        rankingList.add(RankingModel(it.city, it.records.total))
                }
                ranking
            }

            zone != null -> {
                this.repository.findByZone(zone.toLowerCase(), this.pagination(this.sortedByTotal)).forEach {
                    rankingList.add(RankingModel(it.neighbourhood!!, it.records.total))
                }
                ranking
            }

            else -> null
        }
    }

    private fun pagination(sort: Sort): PageRequest = PageRequest.of(0, 5, sort)

    private fun validateToken(token: String?): Unit = token?.let {
        val adjustedToken: String
        if (it.contains("Bearer")) {
            adjustedToken = it.replace("Bearer ", "occurrencesToken ")
            this.validationTokenHelper.validateToken(adjustedToken)
        } else {
            this.validationTokenHelper.validateToken(it)
        }
    } ?: throw InvalidAccessException(this.errorMessageService.getMessage(MISSING_AUTHORIZATION_HEADER))

}