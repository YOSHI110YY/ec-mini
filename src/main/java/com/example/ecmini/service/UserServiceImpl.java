package com.example.ecmini.service;

import com.example.ecmini.entity.User;
import com.example.ecmini.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません: " + id));
    }

    @Override
    public void updateRole(Long id, String role) {
        User user = findById(id);
        user.setRole(role);
        userRepository.save(user);
    }
}
