package com.bvdv.bvdvapi.repository;

import com.bvdv.bvdvapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String email);

    Optional<User> findFirstByUsernameOrEmail(String username, String email);

}
