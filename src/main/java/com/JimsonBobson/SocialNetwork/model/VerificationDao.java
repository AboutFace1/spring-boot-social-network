package com.JimsonBobson.SocialNetwork.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationDao extends CrudRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
}
