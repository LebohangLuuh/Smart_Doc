package com.example.Smart_Doc.repository;

import com.example.Smart_Doc.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.practiceNumber = :practiceNumber AND u.role = 'doctor'")
    Optional<User> findByPracticeNumber(@Param("practiceNumber") String practiceNumber);

    boolean existsByPracticeNumber(String practiceNumber);
}