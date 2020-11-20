package com.ladys.space.occurrences.constants

class ApiConstants private constructor() {
    object Url {
        const val USER_API: String = "https://ladys-space-user-api.herokuapp.com/api/validate/token"
    }

    object Contact {
        const val NAME: String = "Gabriel Gois Andrade"
        const val GITHUB: String = "https://github.com/gabrielgoisandrade"
        const val EMAIL: String = "gabriel.gois.andrade14@gmail.com"
    }

    object Api {
        const val KEY: String = "Occurrences token"
        const val SERVER: String = "http://ladys-space-occurrences-api.herokuapp.com/api"
        const val TITLE: String = "Lady's Space Occurrences API"
        const val DESCRIPTION: String = "Occurrences API"
        const val VERSION: String = "1.0"
        const val MEDIA_TYPE: String = "application/json"
    }

    object Parameters {
        object Ranking {
            const val FILTER: String = "filter"
            const val FILTER_DESCRIPTION: String = "Specify the region."

            const val ZONE: String = "zone"
            const val ZONE_DESCRIPTION: String = "Specify the zone."
        }

        object Occurrences {
            const val NEIGHBOURHOOD: String = "neighbourhood"
            const val NEIGHBOURHOOD_DESCRIPTION: String = "Returns a list with the values of one specific " +
                    "neighbourhood."

            const val CITY: String = "city"
            const val CITY_DESCRIPTION: String = "Returns a list with the values of one specific city."

            const val ZONE: String = "zone"
            const val ZONE_DESCRIPTION: String = "Returns a list of occurrences in a specific zone."

            const val CRITERIA: String = "criteria"
            const val CRITERIA_DESCRIPTION: String = "Specify the neighbourhood or city."
        }

    }

}