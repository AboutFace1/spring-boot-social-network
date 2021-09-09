package com.JimsonBobson.SocialNetwork;

import com.JimsonBobson.SocialNetwork.model.entity.Interest;
import com.JimsonBobson.SocialNetwork.model.entity.Profile;
import com.JimsonBobson.SocialNetwork.model.entity.SiteUser;
import com.JimsonBobson.SocialNetwork.service.InterestService;
import com.JimsonBobson.SocialNetwork.service.ProfileService;
import com.JimsonBobson.SocialNetwork.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
public class ProfileControllerRestTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private InterestService interestService;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser(username = "pexaev@mail.ru")
    public void testSaveAndDeleteInterest() throws Exception {

        String interestText = "some interest here";

        mockMvc.perform(post("/save-interest").param("name", interestText)).andExpect(status().isOk());

        Interest interest = interestService.get(interestText);

        Assert.assertNotNull("Interest should exist", interest);
        Assert.assertEquals("Retrieved interest text should match", interestText, interest.getName());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        SiteUser user = userService.get(email);
        Profile profile = profileService.getUserProfile(user);

        Assert.assertTrue("Profile should contain interest", profile.getInterests().contains(new Interest(interestText)));

        mockMvc.perform(post("/delete-interest").param("name", interestText)).andExpect(status().isOk());

        profile = profileService.getUserProfile(user);

        Assert.assertFalse("Profile should not containt interest", profile.getInterests().contains(new Interest(interestText)));

    }
}
