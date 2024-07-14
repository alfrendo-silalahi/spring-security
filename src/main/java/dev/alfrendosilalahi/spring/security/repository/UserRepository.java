package dev.alfrendosilalahi.spring.security.repository;

import java.util.Optional;

import dev.alfrendosilalahi.spring.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

}
