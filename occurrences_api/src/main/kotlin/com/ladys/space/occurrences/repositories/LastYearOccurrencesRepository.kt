package com.ladys.space.occurrences.repositories

import com.ladys.space.occurrences.models.LastYearOccurrencesModel
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.query.Param

interface LastYearOccurrencesRepository : MongoRepository<LastYearOccurrencesModel, String> {

    fun findByRegionOrderByPoliceStationAsc(
            @Param("region") region: String,
            @Param("sortedBy") sortedBy: Sort
    ): LastYearOccurrencesModel?
}