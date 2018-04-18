package com.ptff.qsystem.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
    
	@Query("select distinct v from Vendor v where name like %:name%")
	Page<Vendor> findBySearchCriteria(@Param("name")String name, Pageable pageable);
	
	@Query("select distinct v from Vendor v where name like %:name% and v.status = :status")
	Page<Vendor> findBySearchCriteria(@Param("name")String name, @Param("status") VendorStatus status, Pageable pageable);
}