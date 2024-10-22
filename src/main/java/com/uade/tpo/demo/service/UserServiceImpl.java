package com.uade.tpo.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.Entity.Role;
import com.uade.tpo.demo.Entity.User;
import com.uade.tpo.demo.repository.UserRepository;
import com.uade.tpo.demo.controllers.users.UserRequest;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(UserRequest userRequest) {
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setName(userRequest.getName());
        user.setPassword(userRequest.getPassword());  
        Role userRole = userRequest.getRole();
        user.setRole(userRole);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, UserRequest userRequest) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setEmail(userRequest.getEmail());
            updatedUser.setName(userRequest.getName());
            Role userRole = userRequest.getRole();
            updatedUser.setRole(userRole);
            return userRepository.save(updatedUser);
        } else {
            throw new RuntimeException("User not found with id " + id);
        }
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new RuntimeException("User not found with id " + id);
        }
    }
    
    
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
