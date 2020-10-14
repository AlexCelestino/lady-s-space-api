package com.ladys.space.api.config

import com.ladys.space.api.security.RequestFilterSecurity
import com.ladys.space.api.services.JwtService
import com.ladys.space.api.services.LoginService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.POST
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var loginService: LoginService

    @Autowired
    private lateinit var jwtService: JwtService

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private lateinit var resolver: HandlerExceptionResolver

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun requestFilter(): OncePerRequestFilter =
            RequestFilterSecurity(this.loginService, this.jwtService, this.resolver)

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(this.loginService).passwordEncoder(this.passwordEncoder())
    }

    override fun configure(http: HttpSecurity): Unit =
            with(http) {
                this.csrf().disable()
                        .authorizeRequests()
                        .antMatchers(POST, "/register", "/login", "/validate/**").permitAll()

                this.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                this.addFilterBefore(requestFilter(), UsernamePasswordAuthenticationFilter::class.java)
            }

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers("/h2-console/**")
    }

}