package com.example.ecmini.service;

import com.example.ecmini.entity.User;
import com.example.ecmini.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

@Service
// Spring がこのクラスを DI 管理し、SecurityConfig から利用できるようにする。

public class CustomUserDetailsService implements UserDetailsService {
    // Spring Security が「ログイン時に呼び出すサービス」として認識する。

    private final UserRepository userRepository;
    // DB からユーザー情報を取得するために必要。

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("LOGIN TRY username=" + username);

        User user = userRepository.findByUsername(username);

        System.out.println("DB USER FOUND=" + (user != null));

        if (user != null) {
            System.out.println("DB ROLE=" + user.getRole());
            System.out.println("DB PASS LENGTH=" + user.getPassword().length());
        }

        if (user == null) {
            throw new UsernameNotFoundException("ユーザーが見つかりません: " + username);
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(AuthorityUtils.createAuthorityList("ROLE_" + user.getRole()))
                .build();
    }
}
