package com.uade.tpo.demo.controllers.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uade.tpo.demo.Entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    @JsonProperty("access_token")
    private String accessToken;
    private Long userId; 
    private Role role;
    private String name;
}
