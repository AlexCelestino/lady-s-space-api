package com.ladys.space.occurrences.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document(collection = "occurrences")
data class OccurrencesModel(
        @Id
        val id: String,
        val region: String,
        val city: String,
        val neighbourhood: String?,
        val zone: String?,
        val records: RecordsModel,
        val updatedAt: LocalDate?
)