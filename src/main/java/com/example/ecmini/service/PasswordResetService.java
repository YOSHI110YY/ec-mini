package com.example.ecmini.service;

import com.example.ecmini.entity.PasswordResetToken;
import com.example.ecmini.entity.User;
import com.example.ecmini.repository.PasswordResetTokenRepository;
import com.example.ecmini.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    public void updatePassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        User user = userRepository.findById(resetToken.getUserId()).orElse(null);

        String encoded = passwordEncoder.encode(newPassword);
        user.setPassword(encoded);
        userRepository.save(user);

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
    }
    public void sendResetMail(String email) {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            return; // メールアドレスが存在しない場合は何もしない
        }

        // トークン生成
        String token = UUID.randomUUID().toString();

        // トークン保存
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUserId(user.getId());
        resetToken.setToken(token);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        resetToken.setUsed(false);
        tokenRepository.save(resetToken);

        // リセットURL
        String resetUrl = "http://localhost:8080/password/reset/form?token=" + token;

        // メール送信
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("パスワード再設定");
        message.setText("以下のURLからパスワードを再設定してください：\n" + resetUrl);

        mailSender.send(message);
    }
    public boolean validateToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);

        if (resetToken == null) {
            return false; // トークンが存在しない
        }

        if (resetToken.isUsed()) {
            return false; // すでに使われている
        }

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false; // 有効期限切れ
        }

        return true; // 有効なトークン
    }



}
