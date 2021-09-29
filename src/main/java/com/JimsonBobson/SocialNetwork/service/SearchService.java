package com.JimsonBobson.SocialNetwork.service;

import com.JimsonBobson.SocialNetwork.model.dto.SearchResult;
import com.JimsonBobson.SocialNetwork.model.entity.Profile;
import com.JimsonBobson.SocialNetwork.model.entity.SiteUser;
import com.JimsonBobson.SocialNetwork.model.repository.ProfileDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Value("${results.pagesize}")
    private int pageSize;

    @Autowired
    private ProfileDao profileDao;

    public List<SearchResult> search(String text, int pageNumber) {

        PageRequest request = PageRequest.of(pageNumber-1, pageSize);

        Page<Profile> results = null;
        if (text.trim().length() == 0) {
//            results = profileDao.findAll((Pageable) request);
        }
        else {
//            results = profileDao.findAllByUserIdNotAndInterestsNameContainingIgnoreCase(text, (Pageable) request);
        }

//        return results.map(p -> new SearchResult(p));

        return profileDao.findAllByUserIdNotAndInterestsNameContainingIgnoreCase(text, (Pageable) request).stream().map(SearchResult::new).collect(Collectors.toList());
    }
}
