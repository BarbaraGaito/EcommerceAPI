package com.uade.tpo.demo.service;

import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.controllers.auth.AuthenticationRequest;
import com.uade.tpo.demo.controllers.auth.AuthenticationResponse;
import com.uade.tpo.demo.controllers.auth.RegisterRequest;
import com.uade.tpo.demo.controllers.config.JwtService;
import com.uade.tpo.demo.Entity.User;
import com.uade.tpo.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
        private final UserRepository repository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;
        public AuthenticationResponse register(RegisterRequest request) {
                if (!EmailValidation.isValidEmail(request.getEmail())) {
                    throw new RuntimeException("Invalid email.");
                }
            
                if (repository.findByEmail(request.getEmail()).isPresent()) {
                    throw new RuntimeException("Email is already registered.");
                }
            
                // Crear el usuario con los datos del request
                var user = User.builder()
                        .name(request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(request.getRole())
                        .build();
            
                // Guardar el usuario en la base de datos
                repository.save(user);
            
                // Generar el token JWT
                var jwtToken = jwtService.generateToken(user);
            
                // Incluir el userId en la respuesta
                return AuthenticationResponse.builder()
                        .accessToken(jwtToken)
                        .userId(user.getId()) // Aquí incluimos el ID del usuario
                        .build();
            }
            

        public AuthenticationResponse authenticate(AuthenticationRequest request) {
                authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(                        
                        request.getEmail(),
                        request.getPassword()));
            
                var user = repository.findByEmail(request.getEmail())
                    .orElseThrow();
                
                // Aquí asegúrate de que el método generateToken incluya el ID del usuario
                var jwtToken = jwtService.generateToken(user);
                
                return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .userId(user.getId()) // Agregar el ID del usuario a la respuesta
                    .role(user.getRole())
                    .name(user.getName())
                    .build();
            }
}
