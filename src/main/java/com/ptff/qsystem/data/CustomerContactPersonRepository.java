package com.ptff.qsystem.data;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerContactPersonRepository extends JpaRepository<CustomerContactPerson, Long> {
    Set<CustomerContactPerson> findAllByCustomerId(Long id);
}