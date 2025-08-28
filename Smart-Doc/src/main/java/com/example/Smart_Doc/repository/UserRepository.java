package com.example.Smart_Doc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.Smart_Doc.model.entity.User;
import java.util.Optional;
import java.time.LocalDateTime;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByResetToken(String resetToken);

    @Query("SELECT u FROM User u WHERE u.resetToken = :token AND u.resetTokenExpiryDate > :now")
    Optional<User> findByResetTokenAndNotExpired(@Param("token") String token, @Param("now") LocalDateTime now);

    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.email = :email")
    void deleteByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.role = :role")
    Optional<User> findByRole(@Param("role") String role);
}