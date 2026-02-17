package com.R8.BloggingPlatform.repositories;

import com.R8.BloggingPlatform.domain.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User,UUID> {
    Optional<User> findByEmail(String email);
}
