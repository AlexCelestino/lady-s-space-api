package com.ladys.space.api.config

import com.ladys.space.api.security.JwtSecurity
import com.ladys.space.api.security.filter.RequestFilter
import com.ladys.space.api.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.*
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
    private lateinit var userService: UserService

    @Autowired
    private lateinit var jwtSecurity: JwtSecurity

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private lateinit var resolver: HandlerExceptionResolver

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun requestFilter(): OncePerRequestFilter = RequestFilter(this.userService, this.jwtSecurity, this.resolver)

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(this.userService).passwordEncoder(this.passwordEncoder())
    }

    override fun configure(http: HttpSecurity): Unit = with(http) {
        this.csrf().disable()
                .authorizeRequests()
                .antMatchers(POST, "/user/**").permitAll()
                .antMatchers(GET, "/validate/email").permitAll()
                .antMatchers(GET, "/validate/token").authenticated()
                .antMatchers(PUT, "/user/update").authenticated()
                .antMatchers(PATCH, "/user/change-password").authenticated()

        this.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        this.addFilterBefore(requestFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers("/h2-console/**")
    }

}