package com.example.sneaker_hub_backend.service;

import com.example.sneaker_hub_backend.model.AppUser;
import com.example.sneaker_hub_backend.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    // private BCryptPasswordEncoder passwordEncoder;

    public Optional<AppUser> findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public AppUser save(AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return appUserRepository.save(user);
    }
    public AppUser updateuser(AppUser user) {
        // user.setPassword(passwordEncoder.encode(user.getPassword()));
        return appUserRepository.save(user);
    }


    public boolean isUsernameTaken(String username) {
        return appUserRepository.findByUsername(username).isPresent();
    }
      

    // Method to validate a user's credentials
    public boolean validateUser(String username, String password ) {
        Optional<AppUser> user = appUserRepository.findByUsername(username);
        if (user.isPresent()) {
            return passwordEncoder.matches(password, user.get().getPassword());
        }
        return false;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("USER") // Customize authorities as needed
                .build();
    }

     public Optional<AppUser> getAppUserById(Long id) {
        return appUserRepository.findById(id);
    }
}
