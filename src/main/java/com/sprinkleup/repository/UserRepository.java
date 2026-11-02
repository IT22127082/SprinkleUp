package com.sprinkleup.repository;

import com.sprinkleup.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/** Looks up any kind of user (customer or admin) by email. */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
}
