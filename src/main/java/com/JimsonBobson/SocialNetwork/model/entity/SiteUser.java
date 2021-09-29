package com.JimsonBobson.SocialNetwork.model.entity;

import com.JimsonBobson.SocialNetwork.validation.PasswordMatch;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
@PasswordMatch(message = "{register.repeatpassword.mismatch}")
public class SiteUser {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true)
    @Email(message = "{register.email.invalid}")
    @NotBlank(message = "Email field should not be empty")
    private String email;

    @Transient
    @Size(min=5, max=15, message = "{register.password.size}")
    private String plainPassword;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private Boolean enabled = false;

    @NotNull
    @Column(name = "firstname", length = 20)
    @Size(min=2, max=20, message = "{register.firstname.size}")
    private String firstname;

    @NotNull
    @Column(name = "surname", length = 25)
    @Size(min=2, max=25, message = "{register.surname.size}")
    private String surname;

    @Transient // Hibernate doesn't save as an object
    private String repeatPassword;

    @Column(name="role", length = 20)
    private String role;

    public SiteUser() {

    }

    public SiteUser(String email, String password, String firstname, String surname) {
        this.email = email;
        this.plainPassword = password;
        this.repeatPassword = password;
        this.enabled = true;
        this.firstname = firstname;
        this.surname = surname;
    }

    public SiteUser(String email, String password, String firstname, String surname, String role) {
        this.email = email;
        this.plainPassword = password;
        this.repeatPassword = password;
        this.enabled = true;
        this.firstname = firstname;
        this.surname = surname;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.password = new BCryptPasswordEncoder().encode(plainPassword);
        this.plainPassword = plainPassword;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String toString() {
        return "SiteUser{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", plainPassword='" + plainPassword + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                ", repeatPassword='" + repeatPassword + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
