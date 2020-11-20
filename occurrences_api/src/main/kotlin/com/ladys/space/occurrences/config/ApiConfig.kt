package com.ladys.space.occurrences.config

import com.ladys.space.occurrences.constants.ApiConstants.Api.DESCRIPTION
import com.ladys.space.occurrences.constants.ApiConstants.Api.KEY
import com.ladys.space.occurrences.constants.ApiConstants.Api.SERVER
import com.ladys.space.occurrences.constants.ApiConstants.Api.TITLE
import com.ladys.space.occurrences.constants.ApiConstants.Api.VERSION
import com.ladys.space.occurrences.constants.ApiConstants.Contact.EMAIL
import com.ladys.space.occurrences.constants.ApiConstants.Contact.GITHUB
import com.ladys.space.occurrences.constants.ApiConstants.Contact.NAME
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER
import io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApiConfig {

    @Bean
    fun api(): OpenAPI = OpenAPI()
            .components(this.components())
            .servers(this.server())
            .info(this.apiInfo())

    private fun components(): Components = Components()
            .addSecuritySchemes(
                    KEY,
                    SecurityScheme()
                            .type(HTTP)
                            .scheme("bearer")
                            .`in`(HEADER)
                            .bearerFormat("JWT")
            )

    private fun server(): List<Server> = listOf(Server()
            .url(SERVER)
            .description("Main server")
    )

    private fun apiInfo(): Info = Info()
            .title(TITLE)
            .description(DESCRIPTION)
            .version(VERSION)
            .contact(this.contact())

    private fun contact(): Contact = Contact()
            .name(NAME)
            .url(GITHUB)
            .email(EMAIL)

}