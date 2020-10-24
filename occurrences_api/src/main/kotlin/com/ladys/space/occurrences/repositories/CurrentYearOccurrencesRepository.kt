package com.ladys.space.occurrences.repositories

import com.ladys.space.occurrences.models.CurrentYearOccurrencesModel
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.query.Param

interface CurrentYearOccurrencesRepository : MongoRepository<CurrentYearOccurrencesModel, String> {

    fun findByRegionOrderByPoliceStationAsc(
            @Param("region") region: String,
            @Param("sortedBy") sortedBy: Sort
    ): CurrentYearOccurrencesModel?
}