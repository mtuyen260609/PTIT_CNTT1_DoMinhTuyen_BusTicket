package com.example.busticket.repository;

import com.example.busticket.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    @EntityGraph(attributePaths = "profile")
    Optional<User> findByUsername(String username);

    @EntityGraph(attributePaths = "profile")
    Optional<User> findWithProfileById(Long id);
}
