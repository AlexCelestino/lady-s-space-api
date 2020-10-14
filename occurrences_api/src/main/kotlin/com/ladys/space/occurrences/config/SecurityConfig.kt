package com.ladys.space.occurrences.config

import com.ladys.space.occurrences.security.FilterSecurity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.GET
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private lateinit var resolver: HandlerExceptionResolver

    @Bean
    fun requestFilter(): OncePerRequestFilter = FilterSecurity(this.resolver)

    override fun configure(http: HttpSecurity): Unit =
            with(http) {
                this.csrf().disable()
                        .authorizeRequests()
                        .antMatchers(GET, "/**", "/ranking/**", "/validate-token").permitAll()

                this.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                this.addFilterBefore(requestFilter(), UsernamePasswordAuthenticationFilter::class.java)
            }

}
