package com.JimsonBobson.SocialNetwork.controllers;


import com.JimsonBobson.SocialNetwork.exceptions.ImageTooSmallException;
import com.JimsonBobson.SocialNetwork.exceptions.InvalidFileException;
import com.JimsonBobson.SocialNetwork.model.FileInfo;
import com.JimsonBobson.SocialNetwork.model.Interest;
import com.JimsonBobson.SocialNetwork.model.Profile;
import com.JimsonBobson.SocialNetwork.model.SiteUser;
import com.JimsonBobson.SocialNetwork.service.FileService;
import com.JimsonBobson.SocialNetwork.service.InterestService;
import com.JimsonBobson.SocialNetwork.service.ProfileService;
import com.JimsonBobson.SocialNetwork.service.UserService;
import com.JimsonBobson.SocialNetwork.status.PhotoUploadStatus;
import org.owasp.html.PolicyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private InterestService interestService;

    @Autowired
    private PolicyFactory htmlPolicy;

    @Autowired
    FileService fileService;

    @Value("${photo.upload.ok}")
    private String photoStatusOK;

    @Value("${photo.upload.invalid}")
    private String photoStatusInvalid;

    @Value("${photo.upload.ioexception}")
    private String photoStatusIOException;

    @Value("${photo.upload.toosmall}")
    private String photoStatusTooSmall;

    @Value("${photo.upload.directory}")
    private String photoUploadDirectory;

    private SiteUser getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        return userService.get(email);
    }

    private ModelAndView showProfile(SiteUser user) {

        ModelAndView modelAndView = new ModelAndView();

        if (user==null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }

        Profile profile = profileService.getUserProfile(user);

        if(profile == null) {
            profile = new Profile();
            profile.setUser(user);
            profileService.save(profile);
        }

        Profile webProfile = new Profile();
        webProfile.safeCopyFrom(profile);

        modelAndView.getModel().put("userId", user.getId());
        modelAndView.getModel().put("profile", webProfile);

        modelAndView.setViewName("app.profile");

        return modelAndView;
    }

    @GetMapping("/profile")
    public ModelAndView showProfile() {

        SiteUser user = getUser();

        ModelAndView modelAndView = showProfile(user);

        modelAndView.getModel().put("ownProfile", true);


        return modelAndView;
    }

    @GetMapping("/profile/{id}")
    public ModelAndView showProfile(@PathVariable("id") Long id) {

        SiteUser user = userService.get(id);

        ModelAndView modelAndView = showProfile(user);

        modelAndView.getModel().put("ownProfile", false);

        return modelAndView;
    }

    @GetMapping("/edit-profile-about")
    public ModelAndView editProfileAbout(ModelAndView modelAndView) {

        SiteUser user = getUser();
        Profile profile = profileService.getUserProfile(user);

        Profile webProfile = new Profile();
        webProfile.safeCopyFrom(profile);

        modelAndView.getModel().put("profile", webProfile);
        modelAndView.setViewName("app.editProfileAbout");

        return modelAndView;
    }

    @PostMapping("/edit-profile-about")
    public ModelAndView editProfileAbout(ModelAndView modelAndView, @Valid Profile webProfile, BindingResult result) {

        modelAndView.setViewName("app.editProfileAbout");

        SiteUser user = getUser();
        Profile profile = profileService.getUserProfile(user);

        profile.safeMergeFrom(webProfile, htmlPolicy);

        if(!result.hasErrors()) {
            profileService.save(profile);
            modelAndView.setViewName("redirect:/profile");
        }

        return modelAndView;
    }

    @PostMapping(value = "/upload-profile-photo")
    @ResponseBody // return data in JSON format
    public ResponseEntity<PhotoUploadStatus> handlePhotoUploads(@RequestParam("file") MultipartFile file) {


        SiteUser user = getUser();
        Profile profile = profileService.getUserProfile(user);

        Path oldPhotoPath = profile.getPhoto(photoUploadDirectory);

        PhotoUploadStatus status = new PhotoUploadStatus(photoStatusOK);

        try {
            FileInfo photoInfo = fileService.saveImageFile(file, photoUploadDirectory, "photos", "p" + user.getId(), 100, 100);

            profile.setPhotoDetails(photoInfo);
            profileService.save(profile);

            if (oldPhotoPath != null) {
                Files.delete(oldPhotoPath);
            }

        } catch (InvalidFileException e) {
            status.setMessage(photoStatusInvalid);
            e.printStackTrace();
        } catch (IOException e) {
            status.setMessage(photoStatusIOException);
            e.printStackTrace();
        } catch (ImageTooSmallException e) {
            status.setMessage(photoStatusTooSmall);
            e.printStackTrace();
        }

        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @GetMapping("/profilephoto/{id}")
    @ResponseBody
    ResponseEntity<InputStreamResource> servePhoto(@PathVariable Long id) throws IOException {
        SiteUser user = userService.get(id);
        Profile profile = profileService.getUserProfile(user);

        Path photoPath = Paths.get(photoUploadDirectory, "default", "lickKing.jpg");

        if (profile != null && profile.getPhoto(photoUploadDirectory) != null) {
            photoPath = profile.getPhoto(photoUploadDirectory);
        }

        return ResponseEntity
                .ok()
                .contentLength(Files.size(photoPath))
                .contentType(MediaType.parseMediaType(URLConnection.guessContentTypeFromName(photoPath.toString())))
                .body(new InputStreamResource(Files.newInputStream(photoPath, StandardOpenOption.READ)));
    }

    @PostMapping("/save-interest")
    @ResponseBody
    public ResponseEntity<?> saveInterest(@RequestParam("name") String interestName) {

        SiteUser user = getUser();
        Profile profile = profileService.getUserProfile(user);

        String cleanedInterestName = htmlPolicy.sanitize(interestName);

        Interest interest = interestService.createIfNotExists(cleanedInterestName);

        profile.addInterest(interest);
        profileService.save(profile);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/delete-interest")
    @ResponseBody
    public ResponseEntity<?> deleteInterest(@RequestParam("name") String interestName) {

        SiteUser user = getUser();
        Profile profile = profileService.getUserProfile(user);

        profile.removeInterest(interestName);

        profileService.save(profile);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
