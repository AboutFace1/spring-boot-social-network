package com.JimsonBobson.SocialNetwork.controllers;

import com.JimsonBobson.SocialNetwork.model.entity.StatusUpdate;
import com.JimsonBobson.SocialNetwork.service.StatusUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class StatuSupdateController {

    @Autowired
    private StatusUpdateService statusUpdateService;

    @GetMapping("/editstatus")
    ModelAndView editStatus(ModelAndView modelAndView, @RequestParam(name = "id") Long id) {

        StatusUpdate statusUpdate = statusUpdateService.get(id);

        modelAndView.getModel().put("statusUpdate", statusUpdate);

        modelAndView.setViewName("app.editStatus");

        return modelAndView;
    }

    @PostMapping("/editstatus")
    ModelAndView editStatus(ModelAndView modelAndView, @Valid StatusUpdate statusUpdate, BindingResult result) {

        modelAndView.setViewName("app.editStatus");

        if (!result.hasErrors())  {            // IF NO VALIDATIONS ERRORS
            statusUpdateService.save(statusUpdate);
            modelAndView.setViewName("redirect:/viewstatus");
        }

        return modelAndView;
    }

    @GetMapping("/deletestatus")
    ModelAndView deleteStatus(ModelAndView modelAndView, @RequestParam(name="id") Long id) {

        statusUpdateService.delete(id);

        modelAndView.setViewName("redirect:/viewstatus");

        return modelAndView;
    }

    @GetMapping("/viewstatus")
    public ModelAndView viewStatus(ModelAndView modelAndView, @RequestParam(name = "page", defaultValue = "1") int pageNumber) {

        Page<StatusUpdate> page = statusUpdateService.getPage(pageNumber);

        modelAndView.setViewName("app.viewStatus");
        modelAndView.getModel().put("page", page);

        return modelAndView;
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
            modelAndView.setViewName("redirect:/viewstatus");
        }



        StatusUpdate latest = statusUpdateService.getLatest();
        modelAndView.getModel().put("latest", latest);


        return modelAndView;
    }
}
