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
                                                .requestMatchers(HttpMethod.POST, "/categories/**").permitAll()//hasAnyAuthority(Role.ADMIN.name())                                                   
                                                //ORDER
                                                .requestMatchers(HttpMethod.GET, "/order/**").permitAll()//hasAnyAuthority(Role.USER.name())
                                                .requestMatchers(HttpMethod.POST,"/oreder/**").permitAll()//hasAnyAuthority(Role.USER.name())
                                                .requestMatchers(HttpMethod.DELETE, "/order/**").permitAll()//hasAnyAuthority(Role.USER.name())
                                                //CARRITO
                                                .requestMatchers("/cart/**").permitAll()//hasAnyAuthority(Role.USER.name())                       
                                                //CATALOGO
                                                .requestMatchers(HttpMethod.GET,"/catalogo/**").permitAll()
                                                .requestMatchers(HttpMethod.POST,"/catalogo/**").permitAll()//hasAnyAuthority(Role.ADMIN.name())                       
                                                //PRODUCTOS
                                                .requestMatchers(HttpMethod.GET,"/products/**").permitAll()
                                                .requestMatchers(HttpMethod.POST,"/products/**").permitAll()//hasAnyAuthority(Role.ADMIN.name())  
                                                .requestMatchers(HttpMethod.DELETE,"/products/**").permitAll()//hasAnyAuthority(Role.ADMIN.name())                     
                                                //USUARIOS (a chequear)
                                                .requestMatchers("/users/**").permitAll()//hasAnyAuthority(Role.ADMIN.name())
                                                .anyRequest()
                                                .authenticated())
                                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}





