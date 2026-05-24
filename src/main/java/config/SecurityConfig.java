package com.example.ecmini.config;
// 目的：設定クラスをまとめるパッケージ。

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.example.ecmini.service.CustomUserDetailsService;
import com.example.ecmini.security.CustomAuthSuccessHandler;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final CustomAuthSuccessHandler customAuthSuccessHandler;

    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          CustomAuthSuccessHandler customAuthSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.customAuthSuccessHandler = customAuthSuccessHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //セキュリティを制限
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // 1. CSRFを有効にする（コメントアウトのままでOK = 有効になります）
                // .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // 【修正箇所】 管理者権限（ADMIN）がないとアクセスできないように変更
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // 【追加】 マイページや注文関連は「ログイン（USER等）」が必要
                        .requestMatchers("/mypage", "/order/**", "/orders/**").authenticated()

                        // 誰でもアクセスできるページ（商品一覧などはここに入れる）
                        .requestMatchers("/", "/login", "/register", "/products/**", "/css/**", "/images/**").permitAll()

                        // それ以外はすべてログインが必要
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .successHandler(customAuthSuccessHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }
    @Bean
    public CommandLineRunner printPassword(PasswordEncoder encoder) {
        return args -> {
            System.out.println("PASSWORD_HASH=" + encoder.encode("password"));
        };
    }

}
