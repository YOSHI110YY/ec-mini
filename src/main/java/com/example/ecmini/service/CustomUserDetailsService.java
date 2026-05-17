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
        // 入力された username を元に DB からユーザーを検索。

        User user = userRepository.findByUsername(username);
        // DB からユーザーを取得。

        if (user == null) {
            throw new UsernameNotFoundException("ユーザーが見つかりません: " + username);
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                // Spring Security が扱える UserDetails に変換。

                .password(user.getPassword())
                // DB に保存されている BCrypt パスワードをそのまま渡す。

                .authorities(AuthorityUtils.createAuthorityList("ROLE_" + user.getRole()))
                // DB の role（USER / ADMIN）を Spring Security の形式（ROLE_USER）に変換。

                .build();
    }
}
