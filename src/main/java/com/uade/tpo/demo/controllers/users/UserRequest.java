package com.uade.tpo.demo.controllers.users;

import com.uade.tpo.demo.Entity.Role;

import lombok.Data;

@Data
public class UserRequest {
    private int id;
    private String email;
    private String password;
    private String name;
    private Role role;

}
