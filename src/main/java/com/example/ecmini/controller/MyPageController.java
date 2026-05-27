package com.example.ecmini.controller;

import com.example.ecmini.entity.User; // Userエンティティをインポート
import com.example.ecmini.repository.UserRepository; // Repositoryをインポート
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor // これをつけると下の userRepository が自動で使えるようになります
public class MyPageController {

    private final UserRepository userRepository; // 追加

    @GetMapping("/mypage")
    public String mypage(Model model, Authentication authentication) {

        if (authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            // 1. ログイン中のユーザー名を取得
            String username = authentication.getName();
            // 2. DBからそのユーザーの情報を丸ごと取得
            User user = userRepository.findByUsername(username);
            if (user == null) {
                throw new RuntimeException("ユーザーが見つかりません");
            }

            // 3. HTML側の ${user?.name} に合わせて、"user" という名前で渡す
            model.addAttribute("user", user);
            model.addAttribute("username", username);
            model.addAttribute("role", user.getRole());

        } else {
            return "redirect:/login";
        }
        return "mypage/index";
    }
}