package com.JimsonBobson.SocialNetwork.model;

import org.owasp.html.PolicyFactory;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "profile")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(targetEntity = SiteUser.class)
    @JoinColumn(name="user_id", nullable = false)
    private SiteUser user;

    @Column(name="about", length = 5000)
    @Size(max=5000, message = "{editprofile.about.size}")
    private String about;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SiteUser getUser() {
        return user;
    }

    public void setUser(SiteUser user) {
        this.user = user;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void saveCopyFrom(Profile other) {
        if(other.about != null) {
            this.about = other.about;
        }
    }

    public void saveMergeFrom(Profile webProfile, PolicyFactory htmlPolicy) {
        if(webProfile.about != null) {
            this.about = htmlPolicy.sanitize(webProfile.about);
        }
    }
}
