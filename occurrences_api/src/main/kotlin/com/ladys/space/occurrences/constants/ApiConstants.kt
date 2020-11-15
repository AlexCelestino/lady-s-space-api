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
        const val SERVER: String = "https://ladys-space-occurrences-api.herokuapp.com/api"
        const val TITLE: String = "Lady's Space Occurrences API"
        const val DESCRIPTION: String = "Occurrences API"
        const val VERSION: String = "1.0"
        const val MEDIA_TYPE: String = "application/json"
    }

    object Parameters {
        object Ranking {
            const val CAPITAL: String = "capital"
            const val CAPITAL_DESCRIPTION: String = "Specifies that the ranking will be with the values registered in " +
                    "the capital."

            const val METROPOLITAN_CITY: String = "metropolitanCity"
            const val METROPOLITAN_CITY_DESCRIPTION: String = "Specifies that the ranking will be with the values " +
                    "registered in the metropolitan city."

            const val CITIES: String = "cities"
            const val CITIES_DESCRIPTION: String = "Specifies that the ranking will be with the values registered out" +
                    " of São Paulo."

            const val ZONE: String = "zone"
            const val ZONE_DESCRIPTION: String = "Specifies that the ranking will be with the values registered in " +
                    "certain zones."
        }

        object AllOccurrences {
            const val NEIGHBOURHOOD: String = "neighbourhood"
            const val NEIGHBOURHOOD_DESCRIPTION: String = "Returns a list with the values of one specific " +
                    "neighbourhood."

            const val CITY: String = "city"
            const val CITY_DESCRIPTION: String = "Returns a list with the values of one specific city."

            const val ZONE: String = "zone"
            const val ZONE_DESCRIPTION: String = "Returns a list of occurrences in a specific zone."
        }

    }

}