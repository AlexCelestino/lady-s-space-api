package com.ladys.space.occurrences.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "last_occurrences")
data class LastYearOccurrencesModel(
        @Id
        val id: String,
        val policeStation: String,
        val region: String,
        val records: RecordsModel
)