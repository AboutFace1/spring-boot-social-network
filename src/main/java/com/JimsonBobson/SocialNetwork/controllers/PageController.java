package com.JimsonBobson.SocialNetwork.controllers;


import com.JimsonBobson.SocialNetwork.model.entity.StatusUpdate;
import com.JimsonBobson.SocialNetwork.service.StatusUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageController {

    @Autowired
    private StatusUpdateService statusUpdateService;

    @Value("${message.error.forbidden}")
    private String accessDeniedMessage;


    @GetMapping("/")
    public ModelAndView home(ModelAndView modelAndView) {

        StatusUpdate statusUpdate = statusUpdateService.getLatest();

        modelAndView.getModel().put("statusUpdate", statusUpdate);
        modelAndView.setViewName("app.homepage");

        return modelAndView;
    }

    @GetMapping("/403")
    public ModelAndView accessDenied(ModelAndView modelAndView) {

        modelAndView.getModel().put("message", accessDeniedMessage);
        modelAndView.setViewName("app.message");
        return modelAndView;
    }


    @GetMapping("/about")
    public String about() {
        return "app.about";
    }


}
