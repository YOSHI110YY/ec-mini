package com.example.ecmini.controller;

import com.example.ecmini.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/password")
public class PasswordResetController {

    private final PasswordResetService resetService;

    @GetMapping("/reset")
    public String resetForm() {
        return "password/reset";
    }

    @PostMapping("/reset")
    public String sendMail(@RequestParam String email) {
        resetService.sendResetMail(email);
        return "password/reset_sent";
    }

    // ★ パスワード再設定フォーム表示
    @GetMapping("/reset/{token}")
    public String newPasswordForm(@PathVariable String token, Model model) {

        boolean valid = resetService.validateToken(token);

        if (!valid) {
            model.addAttribute("error", "無効なトークンです。");
            return "password/reset-error";
        }

        model.addAttribute("token", token);
        return "password/reset_form";
    }

    // ★ パスワード更新処理
    @PostMapping("/reset/{token}")
    public String updatePassword(
            @PathVariable String token,
            @RequestParam String password
    ) {
        resetService.updatePassword(token, password);
        return "password/reset_complete";
    }
}
