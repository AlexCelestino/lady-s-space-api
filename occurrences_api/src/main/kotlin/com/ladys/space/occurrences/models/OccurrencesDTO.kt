package com.ladys.space.occurrences.models

import java.io.Serializable

data class OccurrencesDTO(
        val policeStation: String,
        val region: String,
        val records: RecordsModel
): Serializable