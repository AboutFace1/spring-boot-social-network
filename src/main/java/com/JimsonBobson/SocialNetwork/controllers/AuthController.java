package com.JimsonBobson.SocialNetwork.controllers;

import com.JimsonBobson.SocialNetwork.model.SiteUser;
import com.JimsonBobson.SocialNetwork.model.VerificationToken;
import com.JimsonBobson.SocialNetwork.service.EmailService;
import com.JimsonBobson.SocialNetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.util.Date;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Value("${message.registration.confirmed}")
    private String registrationConfirmedMessage;

    @Value("${message.invalid.user}")
    private String invalidUserMessage;

    @Value("${message.expired.token}")
    private String expiredTokenMessage;

    @GetMapping("/login")
    String admin() {
        return "app.login";
    }

    @GetMapping("/verifyemail")
    String verifyEmail() {
        return "app.verifyemail";
    }

    @GetMapping("/confirmregister")
    ModelAndView registrationConfirmed(ModelAndView modelAndView, @RequestParam("t") String tokenString) {

        VerificationToken token = userService.getVerificationToken(tokenString);

        if(token==null) {
            modelAndView.setViewName("redirect:/invaliduser");
            userService.deleteToken(token);
            return modelAndView;
        }

        Date expiryDate = token.getExpiry();

        if(expiryDate.before(new Date())) {
            modelAndView.setViewName("redirect:/expiredtoken");
            userService.deleteToken(token);
            return modelAndView;
        }

        SiteUser user = token.getUser();

        if(user == null) {
            modelAndView.setViewName("redirect:/invaliduser");
            userService.deleteToken(token);
            return modelAndView;
        }

        userService.deleteToken(token);
        user.setEnabled(true);
        userService.save(user);

        modelAndView.getModel().put("message", registrationConfirmedMessage);
        modelAndView.setViewName("app.message");
        return modelAndView;
    }

    @GetMapping("/invaliduser")
    ModelAndView invalidUser(ModelAndView modelAndView) {

        modelAndView.getModel().put("message", invalidUserMessage);
        modelAndView.setViewName("app.message");
        return modelAndView;
    }

    @GetMapping("/expiredtoken")
    ModelAndView expiredToken(ModelAndView modelAndView) {

        modelAndView.getModel().put("message", expiredTokenMessage);
        modelAndView.setViewName("app.message");
        return modelAndView;
    }

    @GetMapping("/register")
    ModelAndView register(ModelAndView modelAndView) throws FileNotFoundException  {

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

            String token = userService.createEmailVerification(user);

            emailService.sendVerificationEmail(user.getEmail(), token);

            modelAndView.setViewName("redirect:/verifyemail");
        }

        return modelAndView;
    }
}

