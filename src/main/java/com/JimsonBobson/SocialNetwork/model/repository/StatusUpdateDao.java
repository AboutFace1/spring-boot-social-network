package com.JimsonBobson.SocialNetwork.model.repository;

import com.JimsonBobson.SocialNetwork.model.entity.StatusUpdate;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusUpdateDao extends PagingAndSortingRepository<StatusUpdate, Long> {
    StatusUpdate findFirstByOrderByAddedDesc();
}
