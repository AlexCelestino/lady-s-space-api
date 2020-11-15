package com.ladys.space.occurrences.models.dto

import com.ladys.space.occurrences.models.RecordsModel
import java.io.Serializable
import java.time.LocalDate

data class OccurrencesDTO(
        val region: String,
        val city: String,
        val neighbourhood: String?,
        val zone: String?,
        val records: RecordsModel,
        val updatedAt: LocalDate?
) : Serializable