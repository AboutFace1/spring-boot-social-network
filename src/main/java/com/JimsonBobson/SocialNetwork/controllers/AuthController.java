package com.JimsonBobson.SocialNetwork.controllers;

import com.JimsonBobson.SocialNetwork.model.entity.SiteUser;
import com.JimsonBobson.SocialNetwork.model.entity.VerificationToken;
import com.JimsonBobson.SocialNetwork.service.EmailService;
import com.JimsonBobson.SocialNetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.security.Principal;
import java.util.Date;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Value("${message.registration.confirmed}")
    private String registrationConfirmedMessage;

    @Value("${message.registration.noemail.verification}")
    private String noVerificationMessage;

    @Value("${message.registration.email.verification}")
    private String verificationMessage;

    @Value("${message.invalid.user}")
    private String invalidUserMessage;

    @Value("${message.expired.token}")
    private String expiredTokenMessage;

    @Value("${message.session.timeout}")
    private String sessionTimeoutMessage;

    @Value("${mail.enable}")
    private Boolean emailVerificationEnabled;

    @GetMapping("/login")
    String admin() {
        return "app.login";
    }

    @GetMapping("/verifyemail")
    ModelAndView verifyEmail(ModelAndView modelAndView) {

        if (emailVerificationEnabled) {
            modelAndView.getModel().put("message", verificationMessage);
        } else {
            modelAndView.getModel().put("message", noVerificationMessage);
        }

        modelAndView.setViewName("app.verifyemail");

        return modelAndView;
    }

    @GetMapping("/stayloggedin")
    @ResponseBody
    Boolean stayLoggedIn(HttpServletRequest request) {

        HttpSession session = request.getSession();

        // Session should not time out at all.
        session.setMaxInactiveInterval(-1);

        return true;
    }

    @GetMapping("/ajax/statuscheck")
    @ResponseBody
    Boolean checkUserStatus(HttpServletRequest request, Principal principal) {

        System.out.println("Status check requested");

        Boolean isValidSession = request.isRequestedSessionIdValid();
        Boolean isAuthenticated = principal != null;

        //int sessionTimeout = request.getSession().getMaxInactiveInterval();

        //UserStatusCheck statusCheck = new UserStatusCheck(sessionTimeout, isValidSession, isAuthenticated);

        return isAuthenticated && isValidSession;
        //return statusCheck;
    }

    @GetMapping("/confirmregister")
    ModelAndView registrationConfirmed(ModelAndView modelAndView, @RequestParam("t") String tokenString) {

        VerificationToken token = userService.getVerificationToken(tokenString);

        if (token == null) {
            modelAndView.setViewName("redirect:/invaliduser");
            userService.deleteToken(token);
            return modelAndView;
        }

        Date expiryDate = token.getExpiry();

        if (expiryDate.before(new Date())) {
            modelAndView.setViewName("redirect:/expiredtoken");
            userService.deleteToken(token);
            return modelAndView;
        }

        SiteUser user = token.getUser();

        if (user == null) {
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

    @GetMapping("/sessiontimeout")
    ModelAndView sessionTimeout(ModelAndView modelAndView) {

        modelAndView.getModel().put("message", sessionTimeoutMessage);
        modelAndView.setViewName("app.message");
        return modelAndView;
    }

    @GetMapping("/register")
    ModelAndView register(ModelAndView modelAndView) throws FileNotFoundException {

        SiteUser user = new SiteUser();

        modelAndView.getModel().put("user", user);
        modelAndView.setViewName("app.register");
        return modelAndView;
    }

    @PostMapping("/register")
    ModelAndView register(ModelAndView modelAndView, @ModelAttribute(value = "user") @Valid SiteUser user,
                          BindingResult result) {
        modelAndView.setViewName("app.register");

        if (!result.hasErrors()) {
            userService.register(user);

            if (emailVerificationEnabled == false) {
                user.setEnabled(true);
                userService.save(user);
            }

            String token = userService.createEmailVerificationToken(user);

            emailService.sendVerificationEmail(user.getEmail(), token);

            modelAndView.setViewName("redirect:/verifyemail");
        }

        return modelAndView;
    }
}

