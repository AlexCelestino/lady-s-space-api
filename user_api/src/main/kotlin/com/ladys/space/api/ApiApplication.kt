package com.ladys.space.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources

@PropertySources(
        PropertySource("classpath:error-messages.properties"),
        PropertySource("classpath:error-messages_en.properties")
)
@SpringBootApplication
class ApiApplication

fun main(args: Array<String>) {
    runApplication<ApiApplication>(*args)
}
