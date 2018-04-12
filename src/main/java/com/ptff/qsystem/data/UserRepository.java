package com.ptff.qsystem.data;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByFullName(String fullname);

	Optional<User> findOneByUsernameOrEmail(String username, String email);
	List<User> findByUserGroupNameIn(String[] userGroupName);
}