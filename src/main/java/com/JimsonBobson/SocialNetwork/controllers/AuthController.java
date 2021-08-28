package com.JimsonBobson.SocialNetwork.controllers;

import com.JimsonBobson.SocialNetwork.model.SiteUser;
import com.JimsonBobson.SocialNetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    String admin() {
        return "app.login";
    }

    @GetMapping("/register")
    ModelAndView register(ModelAndView modelAndView) {

        SiteUser user = new SiteUser();
        modelAndView.setViewName("app.register");

        modelAndView.getModel().put("user", user);

        return modelAndView;
    }

    @PostMapping("/register")
    ModelAndView register(ModelAndView modelAndView, @Valid SiteUser user, BindingResult result) {

        modelAndView.setViewName("app.register");

        if(!result.hasErrors()) {
            userService.register(user);
            modelAndView.setViewName("redirect:/");

        }

        return modelAndView;
    }
}

