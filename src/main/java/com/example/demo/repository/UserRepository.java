package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findById(UUID id);
    Optional<User> findByIdAndDeletedFalse(UUID id);
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndDeletedFalse(String email);
    boolean existsByEmail(String email);
    List<User> findAllByIdInAndDeletedFalse(List<UUID> ids);
    org.springframework.data.domain.Page<User> findAllByDeletedFalse(org.springframework.data.domain.Pageable pageable);
}
