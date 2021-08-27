package com.JimsonBobson.SocialNetwork.controllers;


import com.JimsonBobson.SocialNetwork.model.StatusUpdate;
import com.JimsonBobson.SocialNetwork.service.StatusUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageController {

    @Autowired
    private StatusUpdateService statusUpdateService;

    @GetMapping("/")
    public ModelAndView home(ModelAndView modelAndView) {
        modelAndView.setViewName("app.homepage");

        StatusUpdate statusUpdate = statusUpdateService.getLatest();

        modelAndView.getModel().put("statusUpdate", statusUpdate);

        return modelAndView;
    }

    @GetMapping("/about")
    public String about() {
        return "app.about";
    }


}
