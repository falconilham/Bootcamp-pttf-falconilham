package com.ptff.qsystem.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByFullName(String fullname);

	Optional<User> findOneByUsernameOrEmail(String username, String email);
}