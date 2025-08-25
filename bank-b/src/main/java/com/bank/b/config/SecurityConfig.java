package com.bank.b.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/actuator/prometheus", "/actuator/health", "/actuator/info").permitAll()
                .anyRequest().authenticated()
            .and()
            .csrf().disable();
        return http.build();
    }
}
