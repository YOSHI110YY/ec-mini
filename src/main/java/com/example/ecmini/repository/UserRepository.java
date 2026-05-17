package com.example.ecmini.repository;
// 目的：Repository 層に属するクラスであることを示すパッケージ。

import com.example.ecmini.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
// 目的：JPA を使って DB にアクセスするためのインターフェース。

public interface UserRepository extends JpaRepository<User, Long> {
    // 目的：User エンティティを DB とやり取りするための Repository。
    // JpaRepository<User, Long> により CRUD が自動生成される。

    User findByUsername(String username);
    // 目的：ログイン時に username でユーザーを検索するためのメソッド。
    // Spring Security の認証処理で必須。
    User findByEmail(String email);

    List<User> findAllByOrderByIdDesc();
}
