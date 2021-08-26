package com.JimsonBobson.SocialNetwork.controllers;

import com.JimsonBobson.SocialNetwork.model.StatusUpdate;
import com.JimsonBobson.SocialNetwork.service.StatusUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Date;

@Controller
public class PageController {

    @Autowired
    private StatusUpdateService statusUpdateService;

    @GetMapping("/")
    public String home() {
        return "app.homepage";
    }

    @GetMapping("/about")
    public String about() {
        return "app.about";
    }

    @GetMapping("/addstatus")
    public ModelAndView addStatus(ModelAndView modelAndView, @ModelAttribute("statusUpdate") StatusUpdate statusUpdate) {

        modelAndView.setViewName("app.addstatus");

        StatusUpdate latest = statusUpdateService.getLatest();

        modelAndView.getModel().put("latest", latest);
        return modelAndView;
    }

    @PostMapping("/addstatus")
    public ModelAndView addStatus(ModelAndView modelAndView, @Valid StatusUpdate statusUpdate, BindingResult result) {

        modelAndView.setViewName("app.addstatus");

        if (!result.hasErrors()) {
            statusUpdateService.save(statusUpdate);
            modelAndView.getModel().put("statusUpdate", new StatusUpdate()); // Blank space after submissison
        }



        StatusUpdate latest = statusUpdateService.getLatest();
        modelAndView.getModel().put("latest", latest);


        return modelAndView;
    }
}
