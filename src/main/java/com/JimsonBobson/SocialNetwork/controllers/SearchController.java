package com.JimsonBobson.SocialNetwork.controllers;

import com.JimsonBobson.SocialNetwork.model.dto.SearchResult;
import com.JimsonBobson.SocialNetwork.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class SearchController {

    @Autowired
    SearchService searchService;

    @PostMapping("/search")
    public ModelAndView search(ModelAndView modelAndView, @RequestParam("s") String text) {

        List<SearchResult> results = searchService.search(text);

        modelAndView.getModel().put("results", results);
        modelAndView.setViewName("app.search");

        return modelAndView;
    }
}
