package com.ladys.space.occurrences.repositories

import com.ladys.space.occurrences.models.LastYearOccurrencesModel
import org.springframework.data.mongodb.repository.MongoRepository

interface LastYearOccurrencesRepository : MongoRepository<LastYearOccurrencesModel, String>