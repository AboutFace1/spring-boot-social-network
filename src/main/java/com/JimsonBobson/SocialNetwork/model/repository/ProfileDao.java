package com.JimsonBobson.SocialNetwork.model.repository;

import com.JimsonBobson.SocialNetwork.model.entity.Profile;
import com.JimsonBobson.SocialNetwork.model.entity.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface ProfileDao extends CrudRepository<Profile, Long> {
    Profile findByUser(SiteUser user);

//    Page<Profile> findAllByUserIdNot(Long id, Pageable request);
    List<Profile> findAllByUserIdNotAndInterestsNameContainingIgnoreCase(String text, Pageable request);
//    Page<Profile> findAll(Pageable request);
}
