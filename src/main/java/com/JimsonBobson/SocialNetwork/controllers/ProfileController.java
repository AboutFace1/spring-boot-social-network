package com.JimsonBobson.SocialNetwork.controllers;


import com.JimsonBobson.SocialNetwork.model.Profile;
import com.JimsonBobson.SocialNetwork.model.SiteUser;
import com.JimsonBobson.SocialNetwork.service.ProfileServic;
import com.JimsonBobson.SocialNetwork.service.UserService;
import org.owasp.html.PolicyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class ProfileController {

    @Autowired
    UserService userService;

    @Autowired
    ProfileServic profileService;

    @Autowired
    PolicyFactory htmlPolicy;

    private SiteUser getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        return userService.get(email);
    }

    @GetMapping("/profile")
    public ModelAndView showProfile(ModelAndView modelAndView) {

        SiteUser user = getUser();
        Profile profile = profileService.getUserProfile(user);

        if(profile == null) {
            profile = new Profile();
            profile.setUser(user);
            profileService.save(profile);
        }

        Profile webProfile = new Profile();
        webProfile.saveCopyFrom(profile);


        modelAndView.getModel().put("profile", webProfile);

        modelAndView.setViewName("app.profile");
        return modelAndView;
    }

    @GetMapping("/edit-profile-about")
    public ModelAndView editProfileAbout(ModelAndView modelAndView) {

        SiteUser user = getUser();
        Profile profile = profileService.getUserProfile(user);

        Profile webProfile = new Profile();
        webProfile.saveCopyFrom(profile);

        modelAndView.getModel().put("profile", webProfile);
        modelAndView.setViewName("app.editProfileAbout");

        return modelAndView;
    }

    @PostMapping("/edit-profile-about")
    public ModelAndView editProfileAbout(ModelAndView modelAndView, @Valid Profile webProfile, BindingResult result) {

        modelAndView.setViewName("app.editProfileAbout");

        SiteUser user = getUser();
        Profile profile = profileService.getUserProfile(user);

        profile.saveMergeFrom(webProfile, htmlPolicy);

        if(!result.hasErrors()) {
            profileService.save(profile);
            modelAndView.setViewName("redirect:/profile");
        }

        return modelAndView;
    }
}
