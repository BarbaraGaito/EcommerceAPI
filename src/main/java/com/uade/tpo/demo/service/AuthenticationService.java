package com.uade.tpo.demo.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.Entity.User;
import com.uade.tpo.demo.controllers.auth.AuthenticationRequest;
import com.uade.tpo.demo.controllers.auth.AuthenticationResponse;
import com.uade.tpo.demo.controllers.auth.RegisterRequest;
import com.uade.tpo.demo.controllers.config.JwtService;
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
            
                
                var user = User.builder()
                        .name(request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(request.getRole())
                        .build();
            
                
                repository.save(user);
            
                
                var jwtToken = jwtService.generateToken(user);
            
                
                return AuthenticationResponse.builder()
                        .accessToken(jwtToken)
                        .userId(user.getId()) 
                        .role(user.getRole())
                        .name(user.getName())
                        .email(user.getEmail())
                        .build();
            }
            

        public AuthenticationResponse authenticate(AuthenticationRequest request) {
                authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(                        
                        request.getEmail(),
                        request.getPassword()));
            
                var user = repository.findByEmail(request.getEmail())
                    .orElseThrow();
                
                
                var jwtToken = jwtService.generateToken(user);
                
                return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .userId(user.getId()) 
                    .role(user.getRole())
                    .name(user.getName())
                    .email(user.getEmail())
                    .build();
            }
}
