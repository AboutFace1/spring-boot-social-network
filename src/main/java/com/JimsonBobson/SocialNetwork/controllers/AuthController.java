package com.JimsonBobson.SocialNetwork.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    String admin() {
        return "app.login";
    }
}
