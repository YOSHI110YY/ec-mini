package com.example.ecmini.controller.admin;

import com.example.ecmini.entity.User;
import com.example.ecmini.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    // 会員一覧
    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users/list";
    }

    // 会員詳細
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "admin/users/detail";
    }

    // 権限更新
    @PostMapping("/update/{id}")
    public String updateRole(
            @PathVariable Long id,
            @RequestParam String role
    ) {
        userService.updateRole(id, role);
        return "redirect:/admin/users/" + id;
    }
}
