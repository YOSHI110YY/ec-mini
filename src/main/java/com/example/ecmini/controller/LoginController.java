package com.example.ecmini.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//このクラスは画面遷移を担当するコントローラ
@Controller
public class LoginController {

    //loginにアクセスしたときにこのメソッドが呼ばれる
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // templates/login.html を返す
    }
}
