package com.ladys.space.occurrences.models.dto

import com.ladys.space.occurrences.models.RankingModel
import java.io.Serializable

data class RankingDTO(
        val first: RankingModel,
        val second: RankingModel,
        val third: RankingModel,
        val fourth: RankingModel,
        val fifth: RankingModel
) : Serializable