package com.JimsonBobson.SocialNetwork.service;

import com.JimsonBobson.SocialNetwork.model.entity.Profile;
import com.JimsonBobson.SocialNetwork.model.repository.ProfileDao;
import com.JimsonBobson.SocialNetwork.model.entity.SiteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    ProfileDao profileDao;

//    @PreAuthorize("isAuthenticated()")
    public void save(Profile profile) {
        profileDao.save(profile);
    }

//    @PreAuthorize("isAuthenticated()")
    public Profile getUserProfile(SiteUser user) {
        return profileDao.findByUser(user);
    }
}
