package com.ladys.space.occurrences.repositories

import com.ladys.space.occurrences.models.OccurrencesModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.query.Param

interface OccurrencesRepository : MongoRepository<OccurrencesModel, String> {

    fun findByNeighbourhoodOrderByUpdatedAtAsc(@Param("neighbourhood") neighbourhood: String): OccurrencesModel?

    fun findByCityOrderByUpdatedAtAsc(@Param("city") city: String): OccurrencesModel?

    fun findByZoneOrderByUpdatedAtAsc(@Param("zone") zone: String): List<OccurrencesModel>?

    @Query("{region : Capital}")
    fun findByRegion(pageable: Pageable): Page<OccurrencesModel>

    @Query("{region : /Grande São Paulo/}")
    fun findByMetropolitanCity(pageable: Pageable): Page<OccurrencesModel>

    fun findByZone(@Param("zone") zone: String, pageable: Pageable): Page<OccurrencesModel>

}