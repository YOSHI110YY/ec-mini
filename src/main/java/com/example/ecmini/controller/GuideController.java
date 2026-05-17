package com.example.ecmini.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/guide")
public class GuideController {

    @GetMapping("/howto")
    public String howto() { return "guide/howto"; }

    @GetMapping("/payment")
    public String payment() { return "guide/payment"; }

    @GetMapping("/shipping")
    public String shipping() { return "guide/shipping"; }

    @GetMapping("/return")
    public String returns() { return "guide/return"; }
}
