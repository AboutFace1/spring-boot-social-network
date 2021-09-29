package com.JimsonBobson.SocialNetwork.service;

import com.JimsonBobson.SocialNetwork.model.dto.SimpleMessage;
import com.JimsonBobson.SocialNetwork.model.entity.Message;
import com.JimsonBobson.SocialNetwork.model.entity.SiteUser;
import com.JimsonBobson.SocialNetwork.model.repository.MessageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageDao messageDao;

    @Async
    public void save(SiteUser fromUser, SiteUser toUser, String text) {
        messageDao.save(new Message(fromUser, toUser, text));
    }

    public List<SimpleMessage> fetchConversatiton(Long fromUserId, Long toUserId, int page) {

        PageRequest request = PageRequest.of(page, 10);

        Slice<Message> conversation = messageDao.fetchConversation(toUserId, fromUserId, request);

        return conversation.map(m -> new SimpleMessage(m, m.getFromUser().getId().compareTo(toUserId) == 0)).getContent();
    }

    public Page<SimpleMessage> fetchMessageListPage(Long toUserId, int pageNumber) {

        PageRequest request = PageRequest.of(pageNumber - 1, 5);

        Page<Message> results = messageDao.findByToUserIdAndReadFalseOrderBySentDesc(toUserId, request);

        return results.map(m -> new SimpleMessage(m, true));
    }

    public Message get(long messageId) {
        return messageDao.findById(messageId).get();
    }

    public void save(Message message) {
        messageDao.save(message);
    }
}
