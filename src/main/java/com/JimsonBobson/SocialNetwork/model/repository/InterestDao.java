package com.JimsonBobson.SocialNetwork.model.repository;

import com.JimsonBobson.SocialNetwork.model.entity.Interest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestDao extends CrudRepository <Interest, Long> {
    Interest findOneByName(String name);
}
