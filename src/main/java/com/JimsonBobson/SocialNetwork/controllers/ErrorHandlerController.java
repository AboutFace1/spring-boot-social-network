package com.JimsonBobson.SocialNetwork.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorHandlerController implements ErrorController {

    @GetMapping("/error")
    public ModelAndView errorHandler(HttpServletRequest req) {

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.getModel().put("message", "Unknown error");
        modelAndView.getModel().put("url", req.getRequestURL());

        modelAndView.setViewName("app.exception");

        return modelAndView;
    }

    public String getErrorPath() {
        return "/error";
    }
}

