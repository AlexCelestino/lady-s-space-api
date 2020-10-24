package com.ladys.space.occurrences.models.dto

import com.ladys.space.occurrences.models.RecordsModel
import java.io.Serializable

data class OccurrencesDTO(
        val policeStation: String,
        val region: String,
        val records: RecordsModel
): Serializable