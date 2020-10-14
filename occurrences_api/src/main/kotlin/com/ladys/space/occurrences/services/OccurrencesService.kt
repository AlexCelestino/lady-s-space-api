package com.ladys.space.occurrences.services

import com.ladys.space.occurrences.models.OccurrencesDTO
import com.ladys.space.occurrences.models.RankingModel
import com.ladys.space.occurrences.models.dto.RankingDTO
import com.ladys.space.occurrences.repositories.CurrentYearOccurrencesRepository
import com.ladys.space.occurrences.repositories.LastYearOccurrencesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class OccurrencesService {

    @Autowired
    private lateinit var currentYearOccurrencesRepository: CurrentYearOccurrencesRepository

    @Autowired
    private lateinit var lastYearOccurrencesRepository: LastYearOccurrencesRepository

    private val validationTokenService: ValidationTokenService = ValidationTokenService()

    private val currentYear: Int = LocalDate.now().year

    private val lastYear: Int = currentYear - 1

    fun getOccurrences(year: Int, token: String): MutableList<OccurrencesDTO>? {
        val sort: Sort = Sort.by(Sort.Direction.ASC, "delegacia")

        val occurrencesDTO: MutableList<OccurrencesDTO> = mutableListOf()

        this.validationTokenService.validateToken(token)

        return when (year) {
            this.currentYear -> {
                this.currentYearOccurrencesRepository.findAll(sort).forEach {
                    occurrencesDTO.add(OccurrencesDTO(it.delegacia, it.regiao, it.registros))
                }
                occurrencesDTO
            }

            this.lastYear -> {
                this.lastYearOccurrencesRepository.findAll(sort).forEach {
                    occurrencesDTO.add(OccurrencesDTO(it.delegacia, it.regiao, it.registros))
                }
                occurrencesDTO
            }

            else -> null
        }
    }

    fun getTopFive(year: Int, token: String): RankingDTO? {
        val rankingList: MutableList<RankingModel> = ArrayList(5)

        val sort: Sort = Sort.by(Sort.Direction.DESC, "registros.total")

        val ranking: RankingDTO by lazy {
            RankingDTO(rankingList[0], rankingList[1], rankingList[2], rankingList[3], rankingList[4])
        }
		
		this.validationTokenService.validateToken(token)
		
        return when (year) {
            this.currentYear -> {
                this.currentYearOccurrencesRepository.findAll(sort).forEach {
                    rankingList.add(RankingModel(it.delegacia.split("-")[1].trim(), it.registros.total))
                }
                ranking
            }

            this.lastYear -> {
                this.lastYearOccurrencesRepository.findAll(sort).forEach {
                    rankingList.add(RankingModel(it.delegacia.split("-")[1].trim(), it.registros.total))
                }
                ranking
            }

            else -> null
        }
    }

}