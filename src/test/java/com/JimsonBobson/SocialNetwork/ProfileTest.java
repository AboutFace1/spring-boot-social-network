package com.JimsonBobson.SocialNetwork;

import com.JimsonBobson.SocialNetwork.model.entity.Interest;
import com.JimsonBobson.SocialNetwork.model.entity.Profile;
import com.JimsonBobson.SocialNetwork.model.entity.SiteUser;
import com.JimsonBobson.SocialNetwork.service.InterestService;
import com.JimsonBobson.SocialNetwork.service.ProfileService;
import com.JimsonBobson.SocialNetwork.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Random;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class ProfileTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private InterestService interestService;

    private SiteUser[] users = {
            new SiteUser("dfskldf@mail.ru", "113113", "dfsdfsfd", "dfsdf"),
            new SiteUser("dfsklddf@mail.ru", "10002000", "dfdf", "dfdfsdfs"),
            new SiteUser("dfskdjdf@mail.ru", "20002000", "dgfsdfsld", "dfsdfsdfs"),
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

            String name = new Random().ints(10, 0, 10).mapToObj(Integer::toString).collect(Collectors.joining(""));
            user.setEmail(name + "@example.com");

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
