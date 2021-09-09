package com.JimsonBobson.SocialNetwork.service;

import com.JimsonBobson.SocialNetwork.model.entity.Interest;
import com.JimsonBobson.SocialNetwork.model.repository.InterestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterestService {
    @Autowired
    private InterestDao interestDao;

    public long count() {
        return interestDao.count();
    }

    public Interest get(String interestName) {
        return interestDao.findOneByName(interestName);
    }

    public void save(Interest interest) {
        interestDao.save(interest);
    }

    public Interest createIfNotExists(String interestText) {
        Interest interest = interestDao.findOneByName(interestText);

        if (interest==null) {
            interest = new Interest(interestText);
            interestDao.save(interest);
        }

        return interest;
    }
}
