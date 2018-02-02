package com.ptff.qsystem.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    Page<UserGroup> findAll(Pageable pageable);
    UserGroup findByName(String name);
}