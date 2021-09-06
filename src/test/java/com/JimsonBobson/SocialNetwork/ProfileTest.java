package com.JimsonBobson.SocialNetwork;

import com.JimsonBobson.SocialNetwork.model.Interest;
import com.JimsonBobson.SocialNetwork.model.Profile;
import com.JimsonBobson.SocialNetwork.model.SiteUser;
import com.JimsonBobson.SocialNetwork.service.InterestService;
import com.JimsonBobson.SocialNetwork.service.ProfileService;
import com.JimsonBobson.SocialNetwork.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.HashSet;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
public class ProfileTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private InterestService interestService;

    private SiteUser[] users = {
            new SiteUser("dfskldf@mail.ru", "113113"),
            new SiteUser("dfsklddf@mail.ru", "10002000"),
            new SiteUser("dfskdjdf@mail.ru", "20002000"),
    };

    String[][] interests = {
            {"music", "guitar_xxxxxxx", "plants"},
            {"music", "music", "philosophy_dfdfdf"},
            {"philosophy_dfdfdf", "footbal"}
    };

    @Test
    public void testInterest() {
        for (int i = 0; i<users.length; i++) {
            SiteUser user = users[i];
            String[] interestArray = interests[i];

            userService.register(user);

            HashSet<Interest> interestSet = new HashSet<>();

            for (String interestText: interestArray) {
                Interest interest = interestService.createIfNotExists(interestText);
                interestSet.add(interest);

                Assert.assertNotNull("Interest should not be null", interest);
                Assert.assertNotNull("Interest should have ID", interest.getId());
                Assert.assertEquals("Text should match", interestText, interest.getName());
            }

            Profile profile = new Profile(user);
            profile.setInterests(interestSet);
            profileService.save(profile);

            Profile retrievedProfile = profileService.getUserProfile(user);

            Assert.assertEquals("Interest sets should match", interestSet, retrievedProfile.getInterests());
        }
    }
}
