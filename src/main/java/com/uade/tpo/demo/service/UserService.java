
package com.uade.tpo.demo.service;

import java.util.List;

import com.uade.tpo.demo.Entity.User;
import com.uade.tpo.demo.controllers.users.UserRequest;

public interface UserService {
    User createUser(UserRequest userRequest);
    User updateUser(Long id, UserRequest userRequest);
    void deleteUser(Long id);
    User getUserById(Long id);
    List<User> getAllUsers();
}
