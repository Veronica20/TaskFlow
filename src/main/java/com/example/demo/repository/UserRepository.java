package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    @Query("""
            SELECT u
            FROM User u
            LEFT JOIN u.profile p
            WHERE u.deleted = false
              AND (
                  :search IS NULL
                  OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))
                  OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
                  OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
              )
            """)
    Page<User> searchUsers(
            @Param("search") String search,
            Pageable pageable
    );
}
