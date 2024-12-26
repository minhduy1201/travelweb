package com.travelweb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travelweb.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByUsername(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
