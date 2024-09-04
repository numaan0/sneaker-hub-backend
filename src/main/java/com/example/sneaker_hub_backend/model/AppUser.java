package com.example.sneaker_hub_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("email")
    private String email;

    @JsonProperty("dob")
    private LocalDate dob;

    @JsonProperty("profile_pic")
    private String profile_pic;

    @JsonProperty("user_type")
    private String user_type;

    @JsonProperty("mobile_number")
    private String mobile_number;

    @JsonProperty("firstName")
    private String firstName = "";  // Default to empty string

    @JsonProperty("lastName")
    private String lastName = "";   // Default to empty string
    @JsonProperty("gender")
    private String gender = "";   // Default to empty string

    // No-argument constructor (required by JPA)
    public AppUser() {}

    // Parameterized constructor
    public AppUser(String username, String password, String email, LocalDate dob, String profile_pic, String user_type, String mobile_number, String firstName, String lastName, String gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.dob = dob;
        this.profile_pic = profile_pic;
        this.user_type = user_type;
        this.mobile_number = mobile_number;
        this.firstName = firstName != null ? firstName : "";  // Default to empty string if null
        this.lastName = lastName != null ? lastName : "";    // Default to empty string if null
        this.gender = gender != null ? gender : "";    // Default to empty string if null
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    public String getProfilePic() { return profile_pic; }
    public void setProfilePic(String profile_pic) { this.profile_pic = profile_pic; }
    public String getUserType() { return user_type; }
    public void setUserType(String user_type) { this.user_type = user_type; }
    public String getMobileNumber() { return mobile_number; }
    public void setMobileNumber(String mobile_number) { this.mobile_number = mobile_number; }
    public String getFirstName() { return firstName; }
    public String getGender() { return gender; }
    public void setFirstName(String firstName) { this.firstName = firstName != null ? firstName : ""; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName != null ? lastName : ""; }
    public void setGender(String gender) { this.gender = gender != null ? gender : ""; }
}
