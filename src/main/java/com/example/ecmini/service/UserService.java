package com.example.ecmini.service;

import com.example.ecmini.entity.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findById(Long id);

    void updateRole(Long id, String role);
}
