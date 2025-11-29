package com.yanader.OAuth2.repository;

import com.yanader.OAuth2.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, String> {
    AppUser findByEmail(String email);
}
