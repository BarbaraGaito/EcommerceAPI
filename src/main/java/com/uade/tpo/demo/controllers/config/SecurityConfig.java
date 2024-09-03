package com.uade.tpo.demo.controllers.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.uade.tpo.demo.Entity.Role;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(req -> req.requestMatchers("/api/v1/auth/**").permitAll()
                                                .requestMatchers("/error/**").permitAll()
                                                //CATEGORIAS
                                                .requestMatchers(HttpMethod.GET, "/categories/**").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/categories/**").hasAnyAuthority(Role.ADMIN.name())                                                   
                                                //CARRITO
                                                .requestMatchers("/api/cart/**").hasAnyAuthority(Role.USER.name())                       
                                                //CATALOGO
                                                .requestMatchers(HttpMethod.GET,"/api/catalog/**").permitAll()
                                                .requestMatchers(HttpMethod.POST,"/api/catalog/**").hasAnyAuthority(Role.ADMIN.name())                       
                                                //PRODUCTOS
                                                .requestMatchers(HttpMethod.GET,"/api/products/**").permitAll()
                                                .requestMatchers(HttpMethod.POST,"/api/products/**").hasAnyAuthority(Role.ADMIN.name())                       
                                                //USUARIOS (a chequear)
                                                .requestMatchers("/api/users/**").hasAnyAuthority(Role.ADMIN.name())
                                                .anyRequest()
                                                .authenticated())
                                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}





