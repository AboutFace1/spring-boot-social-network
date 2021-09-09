package com.JimsonBobson.SocialNetwork.model.repository;

import com.JimsonBobson.SocialNetwork.model.entity.Profile;
import com.JimsonBobson.SocialNetwork.model.entity.SiteUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProfileDao extends CrudRepository<Profile, Long> {
    Profile findByUser(SiteUser user);

    List<Profile> findByInterestsName(String text);
}
