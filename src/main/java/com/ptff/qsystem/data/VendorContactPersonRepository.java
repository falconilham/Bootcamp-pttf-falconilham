package com.ptff.qsystem.data;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorContactPersonRepository extends JpaRepository<VendorContactPerson, Long> {
    Set<VendorContactPerson> findAllByVendorId(Long id);
}