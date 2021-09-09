package com.JimsonBobson.SocialNetwork.service;

import com.JimsonBobson.SocialNetwork.model.dto.SearchResult;
import com.JimsonBobson.SocialNetwork.model.entity.Profile;
import com.JimsonBobson.SocialNetwork.model.repository.ProfileDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private ProfileDao profileDao;

    public List<SearchResult> search(String text) {

        return profileDao.findByInterestsName(text).stream().map(SearchResult::new).collect(Collectors.toList());

    }
}
