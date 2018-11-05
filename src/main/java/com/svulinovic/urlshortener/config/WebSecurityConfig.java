package com.svulinovic.urlshortener.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {

        auth.jdbcAuthentication()
                .dataSource(jdbcTemplate.getDataSource())
                .usersByUsernameQuery("select accountId, password, true from UserAccount where accountId = ?")
                .authoritiesByUsernameQuery("select accountId, 'USER' from UserAccount where accountId = ?")
                .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/register", "/statistic/**")
                .authenticated()
                .and()
                .httpBasic();

        http.headers().frameOptions().sameOrigin();
    }

}
