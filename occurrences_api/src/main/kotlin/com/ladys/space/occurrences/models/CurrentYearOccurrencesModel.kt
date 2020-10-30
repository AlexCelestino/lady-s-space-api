package com.ladys.space.occurrences.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document(collection = "current_occurrences")
data class CurrentYearOccurrencesModel(
        @Id
        val id: String,
        val policeStation: String,
        val region: String,
        val records: RecordsModel,
        val updatedAt: String?
)