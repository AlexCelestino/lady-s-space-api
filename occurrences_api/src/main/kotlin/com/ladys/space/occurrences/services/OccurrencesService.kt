package com.ladys.space.occurrences.services

import com.ladys.space.occurrences.errors.exceptions.InvalidAccessException
import com.ladys.space.occurrences.models.RankingModel
import com.ladys.space.occurrences.models.dto.OccurrencesDTO
import com.ladys.space.occurrences.models.dto.RankingDTO
import com.ladys.space.occurrences.repositories.CurrentYearOccurrencesRepository
import com.ladys.space.occurrences.repositories.LastYearOccurrencesRepository
import com.ladys.space.occurrences.services.ErrorMessageService.Keys.MISSING_AUTHORIZATION_HEADER
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.stream.Collectors

@Service
class OccurrencesService {

    @Autowired
    private lateinit var currentOccurrencesRepository: CurrentYearOccurrencesRepository

    @Autowired
    private lateinit var lastOccurrencesRepository: LastYearOccurrencesRepository

    @Autowired
    private lateinit var errorMessageService: ErrorMessageService

    private val validationTokenHelper: ValidationTokenHelper by lazy {
        ValidationTokenHelper(this.errorMessageService)
    }

    private val currentYear: Int = LocalDate.now().year

    private val lastYear: Int = currentYear - 1

    private val sortedByPoliceStation: Sort = Sort.by(Sort.Direction.ASC, "police_station")

    private val sortedByTotal: Sort = Sort.by(Sort.Direction.DESC, "records.total")

    fun getOccurrences(year: Int, region: String?, token: String?): List<OccurrencesDTO>? {
        token?.let {
            this.validationTokenHelper.validateToken(it)
        } ?: throw InvalidAccessException(this.errorMessageService.getMessage(MISSING_AUTHORIZATION_HEADER))

        if (region != null)
            return when (year) {
                this.currentYear -> this.currentOccurrencesRepository.findByRegionOrderByPoliceStationAsc(
                        region, this.sortedByPoliceStation
                )?.let {
                    val date: LocalDate? = if (it.updatedAt != null) LocalDate.parse(it.updatedAt) else null
                    listOf(OccurrencesDTO(it.policeStation, it.region, it.records, date))
                }

                this.lastYear -> this.lastOccurrencesRepository.findByRegionOrderByPoliceStationAsc(
                        region, this.sortedByPoliceStation
                )?.let {
                    val date: LocalDate? = if (it.updatedAt != null) LocalDate.parse(it.updatedAt) else null
                    listOf(OccurrencesDTO(it.policeStation, it.region, it.records, date))
                }

                else -> null
            }
        else
            return when (year) {
                this.currentYear -> this.currentOccurrencesRepository.findAll(this.sortedByPoliceStation).stream()
                        .map {
                            val date: LocalDate? = if (it.updatedAt != null) LocalDate.parse(it.updatedAt) else null
                            OccurrencesDTO(it.policeStation, it.region, it.records, date)
                        }.collect(Collectors.toList())

                this.lastYear -> this.lastOccurrencesRepository.findAll(this.sortedByPoliceStation).stream()
                        .map {
                            val date: LocalDate? = if (it.updatedAt != null) LocalDate.parse(it.updatedAt) else null
                            OccurrencesDTO(it.policeStation, it.region, it.records, date)
                        }.collect(Collectors.toList())

                else -> null
            }
    }

    fun getTopFive(year: Int, token: String?): RankingDTO? {
        token?.let {
            this.validationTokenHelper.validateToken(it)
        } ?: throw InvalidAccessException(this.errorMessageService.getMessage(MISSING_AUTHORIZATION_HEADER))

        val rankingList: MutableList<RankingModel> = ArrayList(5)

        val ranking: RankingDTO by lazy {
            RankingDTO(rankingList[0], rankingList[1], rankingList[2], rankingList[3], rankingList[4])
        }


        return when (year) {
            this.currentYear -> {
                this.currentOccurrencesRepository.findAll(this.sortedByTotal).forEach {
                    rankingList.add(RankingModel(it.region, it.records.total))
                }
                ranking
            }

            this.lastYear -> {
                this.lastOccurrencesRepository.findAll(this.sortedByTotal).forEach {
                    rankingList.add(RankingModel(it.region, it.records.total))
                }
                ranking
            }

            else -> null
        }
    }

}