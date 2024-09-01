package com.uade.tpo.demo.controllers.users;

import lombok.Data;

@Data
public class UserRequest {
    private int id;
    private String description;
    private String email;
    private String password;
    private String name;
    private String firstName;
    private String lastname;
    private String Usertype;


}
