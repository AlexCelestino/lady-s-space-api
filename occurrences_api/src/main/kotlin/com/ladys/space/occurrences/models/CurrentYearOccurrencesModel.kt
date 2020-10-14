package com.ladys.space.occurrences.models

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "current_year_occurrences")
data class CurrentYearOccurrencesModel(
        @Id
        val id: String,
        @JsonProperty("policeStation")
        val delegacia: String,
        @JsonProperty("region")
        val regiao: String,
        @JsonProperty("records")
        val registros: RecordsModel
)