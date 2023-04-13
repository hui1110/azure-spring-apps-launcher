package com.azure.spring.apps.launcher.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeHttpRequests()
//            .anyRequest().permitAll();
//        http.authorizeHttpRequests().requestMatchers(
//                        "/", "/assets/**", "/sba-settings.js",
//                        "/extensions/**", "/variables.css", "/applications/**",
//                        "/instances/**", "/deploy/**", "/login/**", "/github/**"
//                    ).permitAll()
            .anyRequest().authenticated()
                         .and().oauth2Login();
        return http.build();
    }

}




