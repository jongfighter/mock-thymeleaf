package com.example.demo

import lombok.extern.slf4j.Slf4j
import org.springframework.beans.BeansException
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter


@Slf4j
@Configuration
class WebConfig : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: WebSecurity) {
        http.ignoring().antMatchers("/**")
    }
}