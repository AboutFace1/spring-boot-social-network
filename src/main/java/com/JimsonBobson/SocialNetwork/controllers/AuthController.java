package com.JimsonBobson.SocialNetwork.controllers;

import com.JimsonBobson.SocialNetwork.model.SiteUser;
import com.JimsonBobson.SocialNetwork.service.EmailService;
import com.JimsonBobson.SocialNetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/login")
    String admin() {
        return "app.login";
    }

    @GetMapping("/verifyemail")
    String verifyEmail() {
        return "app.verifyemail";
    }

    @GetMapping("/register")
    ModelAndView register(ModelAndView modelAndView) {

        SiteUser user = new SiteUser();
        modelAndView.setViewName("app.register");
        modelAndView.getModel().put("user", user);

        return modelAndView;
    }

    @PostMapping("/register")
    ModelAndView register(ModelAndView modelAndView, @ModelAttribute(value = "user") @Valid SiteUser user, BindingResult result) {

        modelAndView.setViewName("app.register");

        if(!result.hasErrors()) {
            userService.register(user);
            emailService.sendVerificationEmail(user.getEmail());

            modelAndView.setViewName("redirect:/verifyemail");
        }

        return modelAndView;
    }
}

