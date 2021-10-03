package com.JimsonBobson.SocialNetwork.controllers;

import com.JimsonBobson.SocialNetwork.model.dto.ChatRequest;
import com.JimsonBobson.SocialNetwork.model.dto.SimpleMessage;
import com.JimsonBobson.SocialNetwork.model.entity.Message;
import com.JimsonBobson.SocialNetwork.model.entity.SiteUser;
import com.JimsonBobson.SocialNetwork.service.MessageService;
import com.JimsonBobson.SocialNetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private Util util;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @GetMapping("/markread")
    String expiredToken(@RequestParam("f") long userId, @RequestParam("m") long messageId) {

        Message message = messageService.get(messageId);

        message.setRead(true);
        messageService.save(message);

        return "redirect:/chatview/" + userId;
    }

    @GetMapping("/messages")
    ModelAndView expiredToken(ModelAndView modelAndView, @RequestParam("p") int pageNumber) {


        SiteUser user = util.getUser();
        Page<SimpleMessage> messages = messageService.fetchMessageListPage(user.getId(), pageNumber);

        modelAndView.getModel().put("messageList", messages);
        modelAndView.getModel().put("pageNumber", pageNumber);
        modelAndView.getModel().put("userId", user.getId());

        modelAndView.setViewName("app.checkmessages");
        return modelAndView;
    }

    @RequestMapping(value = "/conversation/{otherUserId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    List<SimpleMessage> fetchConversation(@PathVariable("otherUserId") Long otherUserId, @RequestBody ChatRequest request) {

        SiteUser thisUser = util.getUser();

        List<SimpleMessage> list = messageService.fetchConversatiton(thisUser.getId(), otherUserId, request.getPage());

        return list;
    }

    @GetMapping("/chatview/{chatWithUserID}")
    ModelAndView chatView(ModelAndView modelAndView, @PathVariable Long chatWithUserID) {

        SiteUser thisUser = util.getUser();
        String chattingWithName = userService.getUserName(chatWithUserID);


        modelAndView.setViewName("chat.chatview");

        modelAndView.getModel().put("thisUserID", thisUser.getId());
        modelAndView.getModel().put("chatWithUserID", chatWithUserID);
        modelAndView.getModel().put("chattingWithName", chattingWithName);

        return modelAndView;
    }

    @MessageMapping("/message/send/{toUserId}")
    public void send(Principal principal, SimpleMessage message, @DestinationVariable Long toUserId) {
        System.out.println(message);

        // Get details for sending user (current user)
        String fromUsername = principal.getName();
        SiteUser fromUser = userService.get(fromUsername);
        Long fromUserId = fromUser.getId();

        // Get details for user we are chatting with
        SiteUser toUser = userService.get(toUserId);
        String toUsername = toUser.getEmail();

        String returnReceiptQueue = String.format("/queue/%d", toUserId);
        String toUserQueue = String.format("/queue/%d", fromUserId);

        messageService.save(fromUser, toUser, message.getText());

        message.setIsReply(false);
        simpMessagingTemplate.convertAndSendToUser(fromUsername, returnReceiptQueue, message);

        message.setFrom(fromUser.getFirstname() + " " + fromUser.getSurname());
        
        message.setIsReply(true);
        simpMessagingTemplate.convertAndSendToUser(toUsername, toUserQueue, message);

        // Send a new message notification
        simpMessagingTemplate.convertAndSendToUser(toUsername, "/queue/newmessages", message);
    }
}
