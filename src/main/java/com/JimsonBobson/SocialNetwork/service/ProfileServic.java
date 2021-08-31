package com.JimsonBobson.SocialNetwork.service;

import com.JimsonBobson.SocialNetwork.model.Profile;
import com.JimsonBobson.SocialNetwork.model.ProfileDao;
import com.JimsonBobson.SocialNetwork.model.SiteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileServic {

    @Autowired
    ProfileDao profileDao;

    public void save(Profile profile) {
        profileDao.save(profile);
    }

    public Profile getUserProfile(SiteUser user) {
        return profileDao.findByUser(user);
    }
}
