package com.ladys.space.occurrences.repositories

import com.ladys.space.occurrences.models.CurrentYearOccurrencesModel
import org.springframework.data.mongodb.repository.MongoRepository

interface CurrentYearOccurrencesRepository : MongoRepository<CurrentYearOccurrencesModel, String>