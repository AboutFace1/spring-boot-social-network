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
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
//@Transactional
public class BulkTests {

    private final static int NUM_USERS = 400;

    private static final String namesFile = "/data/names.txt";
    private static final String interestsFile = "/data/hobbies.txt";

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private InterestService interestService;

    private SiteUser[] testUsers = {
            new SiteUser("pexaev@mail.ru", "sometest", "test", "test")
    };

    private List<String> loadFile(String filename, int maxLength) throws IOException {
        File filePath = new ClassPathResource(filename).getFile();

        Stream<String> stream = Files.lines(filePath.toPath());

        List<String> items = stream
                .filter(line -> !line.isEmpty())
                .map(line -> line.trim())
                .filter(line -> line.length() <= maxLength)
                .map(line -> line.substring(0, 1).toUpperCase() + line.substring(1).toLowerCase())
                .collect(Collectors.toList());

        stream.close();

        return items;
    }

    @Test
    public void createSpecificUsers() {
        for (SiteUser user: testUsers) {

            SiteUser existingUser = userService.get(user.getEmail());

            if (existingUser != null) {
                continue;
            }

            user.setEnabled(true);

            String role = user.getRole();
            if (role == null) {
                user.setRole("ROLE_USER");
            }

            userService.save(user);

            user = userService.get(user.getEmail());

            Profile profile = new Profile(user);
            profileService.save(profile);
        }
    }

    @Test
    public void createTestUsers() throws IOException {
        Random random = new Random();

        List<String> names = loadFile(namesFile, 25);
        List<String> interests = loadFile(interestsFile, 25);

        for (int numUsers=0; numUsers<NUM_USERS; numUsers++) {
            String firstname = names.get(random.nextInt(names.size()));
            String surname = names.get(random.nextInt(names.size()));

            String email = firstname.toLowerCase() + surname.toLowerCase() + "@example.com";

            if(userService.get(email) != null) {
                continue;
            }

            String password = "pass" + firstname.toLowerCase();
            password = password.substring(0, Math.min(15, password.length()));

            Assert.assertTrue(password.length() <= 15);

            SiteUser user = new SiteUser(email, password, firstname, surname);
            user.setEnabled(random.nextInt(5) != 0);

            userService.register(user);

            Profile profile = new Profile(user);

            int numberInterests = random.nextInt(7);

            Set<Interest> userInterests = new HashSet<>();

            for (int i=0; i<numberInterests; i++) {
                String interestText = interests.get(random.nextInt(interests.size()));

                Interest interest = interestService.createIfNotExists(interestText);
                userInterests.add(interest);
            }

            profile.setInterests(userInterests);
            profileService.save(profile);
        }

        Assert.assertTrue(true);
    }
}


