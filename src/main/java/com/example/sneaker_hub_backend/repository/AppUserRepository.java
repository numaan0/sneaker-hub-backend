package com.example.sneaker_hub_backend.repository;

import com.example.sneaker_hub_backend.model.AppUser;

// import org.hibernate.mapping.List;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    List<AppUser> findAll();
}
