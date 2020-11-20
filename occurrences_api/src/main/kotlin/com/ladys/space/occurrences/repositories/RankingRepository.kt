package com.ladys.space.occurrences.repositories

import com.ladys.space.occurrences.models.OccurrencesModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.query.Param

interface RankingRepository : MongoRepository<OccurrencesModel, String> {

    @Query("{region : Capital}")
    fun findAllByCapital(pageable: Pageable): Page<OccurrencesModel>

    fun findByRegionAndZone(
            @Param("region") region: String = "Capital",
            @Param("zone") zone: String,
            pageable: Pageable): Page<OccurrencesModel>

    @Query("{region : /Grande São Paulo/}")
    fun findAllByMetropolitanCity(pageable: Pageable): Page<OccurrencesModel>

}