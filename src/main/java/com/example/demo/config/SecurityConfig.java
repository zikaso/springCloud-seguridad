package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(AuthenticationManagerBuilder authBuilder) throws Exception {

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {   // disbale check for CSRF attack -- >> Spring hide code for inputs Forms
        http.csrf().disable();
        // disbale check for  iframe in html page
        http.headers().frameOptions().disable();

        http.authorizeRequests().anyRequest().permitAll();
    }


}
