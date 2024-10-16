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
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Habilitar CORS
            .authorizeHttpRequests(req -> req
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/error/**").permitAll()
                // CATEGORÍAS
                .requestMatchers(HttpMethod.GET, "/categories/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/categories/**").permitAll()
                // ÓRDENES
                .requestMatchers(HttpMethod.GET, "/order/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/order/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/order/**").permitAll()
                // CARRITO
                .requestMatchers("/cart/**").permitAll()
                // CATÁLOGO
                .requestMatchers(HttpMethod.GET, "/catalogo/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/catalogo/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/catalogo/**").permitAll()
                // PRODUCTOS
                .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/products/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/products/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/products/**").permitAll()
                // USUARIOS
                .requestMatchers("/users/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Configuración global de CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // Permitir el origen del frontend
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE")); // Métodos permitidos
        configuration.setAllowedHeaders(List.of("*")); // Permitir todos los headers
        configuration.setAllowCredentials(true); // Permitir envío de credenciales

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplicar la configuración a todas las rutas
        return source;
    }
}
