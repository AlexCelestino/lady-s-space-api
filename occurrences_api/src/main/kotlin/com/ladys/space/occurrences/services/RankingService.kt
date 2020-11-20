package com.ladys.space.occurrences.services

import com.ladys.space.occurrences.enums.RankingFilterEnum.*
import com.ladys.space.occurrences.models.RankingModel
import com.ladys.space.occurrences.repositories.RankingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.stereotype.Service
import java.util.stream.Collectors


@Service
class RankingService {

    @Autowired
    private lateinit var mRepository: RankingRepository

    @Autowired
    private lateinit var mMessage: ErrorMessageService

    private val validationTokenHelper: ValidationTokenHelper by lazy {
        ValidationTokenHelper(this.mMessage)
    }

    private val sortedByTotal: Sort = Sort.by(DESC, "records.total")

    fun getRanking(token: String?, filter: String?, zone: String?): List<RankingModel>? {
        this.validationTokenHelper.validateToken(token)
        return filter?.let {

            when (filter) {
                CAPITAL.filter -> if (zone != null)
                    this.mRepository.findByRegionAndZone(zone = zone, pageable = this.pagination(this.sortedByTotal))
                            .content.stream().map {
                                RankingModel(it.neighbourhood!!, it.records.total)
                            }.collect(Collectors.toList())
                else
                    this.mRepository.findAllByCapital(this.pagination(this.sortedByTotal)).content.stream().map {
                        RankingModel(it.neighbourhood!!, it.records.total)
                    }.collect(Collectors.toList())

                METROPOLITAN_REGION.filter ->
                    this.mRepository.findAllByMetropolitanCity(this.pagination(this.sortedByTotal)).stream().map {
                        RankingModel(it.city, it.records.total)
                    }.collect(Collectors.toList())

                OTHER_CITIES.filter -> {
                    this.mRepository.findAll(this.sortedByTotal).filter {
                        it.region != "Capital" && it.region != "Grande São Paulo (exclui a Capital)"
                    }.stream().map { RankingModel(it.city, it.records.total) }.collect(Collectors.toList()).run {
                        listOf(
                                this.component1(),
                                this.component2(),
                                this.component3(),
                                this.component4(),
                                this.component5()
                        )
                    }

                }

                else -> null
            }
        } ?: this.mRepository.findAll(this.pagination(this.sortedByTotal)).stream().map {
            RankingModel(it.city, it.records.total)
        }.collect(Collectors.toList())
    }

    private fun pagination(sort: Sort): PageRequest = PageRequest.of(0, 5, sort)

}