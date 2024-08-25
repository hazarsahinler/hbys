package com.example.demo.config;

import com.example.demo.Enum.Role;
import com.example.demo.filter.MyFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final MyFilter myFilter;

    public SecurityConfig(MyFilter myFilter) {
        this.myFilter = myFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       return http.authorizeHttpRequests(req ->
               req.requestMatchers("/kullanici/**").permitAll()
                       .requestMatchers("/api/**").hasAuthority("ROLE_PERSONEL")
                       .anyRequest().authenticated())
               .addFilterBefore(myFilter, UsernamePasswordAuthenticationFilter.class)
               .csrf(AbstractHttpConfigurer::disable)
               .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .build();
    }
}
