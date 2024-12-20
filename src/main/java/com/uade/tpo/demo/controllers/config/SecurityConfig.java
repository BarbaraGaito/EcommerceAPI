package com.uade.tpo.demo.controllers.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.uade.tpo.demo.Entity.Role;

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
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) 
            .authorizeHttpRequests(req -> req
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/error/**").permitAll()
                // CATEGORÍAS
                .requestMatchers(HttpMethod.GET, "/categories/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/categories/admin/**").hasAnyAuthority(Role.ADMIN.name())
                .requestMatchers(HttpMethod.POST, "/categories/**").hasAnyAuthority(Role.ADMIN.name())
                .requestMatchers(HttpMethod.PUT, "/categories/**").hasAnyAuthority(Role.ADMIN.name())
                .requestMatchers(HttpMethod.DELETE, "/categories/**").hasAnyAuthority(Role.ADMIN.name())
                // ÓRDENES
                .requestMatchers(HttpMethod.GET, "/order/**").hasAnyAuthority(Role.ADMIN.name())
                .requestMatchers(HttpMethod.POST, "/order/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/order/**").hasAnyAuthority(Role.ADMIN.name())
                // CARRITO
                .requestMatchers(HttpMethod.GET, "/cart/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/cart/getAll/**").hasAnyAuthority(Role.ADMIN.name())
                .requestMatchers(HttpMethod.GET, "/cart/{id}/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/cart/user/{userId}/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/cart/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/cart/**").permitAll()           
                // CATÁLOGO
                .requestMatchers(HttpMethod.GET, "/catalogo/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/catalogo/**").hasAnyAuthority(Role.USER.name(),Role.ADMIN.name())
                .requestMatchers(HttpMethod.POST, "/catalogo/**").hasAnyAuthority(Role.ADMIN.name())
                .requestMatchers(HttpMethod.DELETE, "/catalogo/**").permitAll()
                // PRODUCTOS
                .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/products/**").hasAnyAuthority(Role.ADMIN.name())
                .requestMatchers(HttpMethod.DELETE, "/products/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/products/**").hasAnyAuthority(Role.ADMIN.name())
                // USUARIOS
                .requestMatchers(HttpMethod.GET, "/users/**").hasAnyAuthority(Role.ADMIN.name())
                .requestMatchers(HttpMethod.PUT, "/users/**").hasAnyAuthority(Role.ADMIN.name())
                .requestMatchers(HttpMethod.DELETE, "/users/**").hasAnyAuthority(Role.ADMIN.name())
                .requestMatchers(HttpMethod.POST, "/users/**").permitAll() 
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); 
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE")); 
        configuration.setAllowedHeaders(List.of("*")); 
        configuration.setAllowCredentials(true); 

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); 
        return source;
    }
}
