package com.uade.tpo.demo.controllers.config;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${application.security.jwt.secretKey}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    // Genera un token JWT para el UserDetails proporcionado
    public String generateToken(UserDetails userDetails) {
        return buildToken(userDetails, jwtExpiration);
    }

    // Construye un token con la expiración proporcionada e incluye roles
    private String buildToken(UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // Username
                .setIssuedAt(new Date(System.currentTimeMillis())) // Fecha de emisión
                .claim("roles", getRolesFromUser(userDetails)) // Agrega los roles como claim
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Fecha de expiración
                .signWith(getSecretKey()) // Firma el token con la clave secreta
                .compact();
    }

    // Verifica si el token es válido comparando el nombre de usuario y si no ha expirado
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Verifica si el token ha expirado
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    // Extrae el nombre de usuario del token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extrae cualquier claim del token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extrae todos los claims del token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder() // Cambiado a parserBuilder
                .setSigningKey(getSecretKey()) // Usa la clave secreta
                .build() // Crea el parser
                .parseClaimsJws(token) // Analiza el token firmado
                .getBody(); // Devuelve el cuerpo de los claims
    }

    // Obtiene la clave secreta en formato SecretKey
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // Extrae los roles del UserDetails y los convierte en una lista de strings
    private List<String> getRolesFromUser(UserDetails userDetails) {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        return authorities.stream()
                .map(GrantedAuthority::getAuthority) // Convierte cada autoridad en un string
                .collect(Collectors.toList());
    }
}
