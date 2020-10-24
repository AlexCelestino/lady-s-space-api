package com.ladys.space.api.config

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.util.*

@Configuration
class ErrorMessagesConfig : WebMvcConfigurer {

    /**
     * Configure basename and encoding to error-messages_pt.properties.
     * @return MessageSource
     * */
    @Bean
    fun messageSource(): MessageSource = ReloadableResourceBundleMessageSource().apply {
        setBasename("classpath:error-messages")
        setDefaultEncoding("ISO-8859-1")
    }

    /**
     * Makes the values of error-messages_pt.properties available.
     * @return LocalValidatorFactoryBean
     * */
    @Bean
    fun validatorFactoryBean(): LocalValidatorFactoryBean? = LocalValidatorFactoryBean().apply {
        setValidationMessageSource(messageSource())
    }

    /**
     * Sets the session location.
     * @return SessionLocaleResolver
     * */
    @Bean
    fun localeResolver(): SessionLocaleResolver = SessionLocaleResolver().apply {
        setDefaultLocale(Locale.ENGLISH)
    }

    /**
     * Makes a language interceptor for the requests.
     * @return LocalValidatorFactoryBean
     * */
    @Bean
    fun localeInterceptor(): LocaleChangeInterceptor = LocaleChangeInterceptor().apply {
        this.paramName = "lang"
    }

    /**
     * Add the interceptor.
     * */
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(localeInterceptor())
    }

}