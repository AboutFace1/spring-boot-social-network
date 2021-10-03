package com.JimsonBobson.SocialNetwork.model.repository;

import com.JimsonBobson.SocialNetwork.model.entity.VerificationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationDao extends CrudRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
    void deleteByUserId(Long id);
}
