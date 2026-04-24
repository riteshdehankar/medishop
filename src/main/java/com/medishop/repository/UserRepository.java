package com.medishop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medishop.OnlineMedicineShop.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}