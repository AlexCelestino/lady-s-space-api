package com.ladys.space.occurrences.repositories

import com.ladys.space.occurrences.models.OccurrencesModel
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.query.Param

interface OccurrencesRepository : MongoRepository<OccurrencesModel, String> {

    fun findByNeighbourhood(@Param("neighbourhood") neighbourhood: String): OccurrencesModel?

    fun findByCity(@Param("city") city: String): OccurrencesModel?

    fun findByRegionAndZone(
            @Param("region") region: String = "Capital",
            @Param("zone") zone: String
    ): List<OccurrencesModel>

    @Query("{region : Capital}")
    fun findByCapital(): List<OccurrencesModel>

    @Query("{region : /Grande São Paulo/}")
    fun findAllByMetropolitanCity(): List<OccurrencesModel>

}
