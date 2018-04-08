package com.ptff.qsystem.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemAdditionalRepository extends JpaRepository<ItemAdditional, Long> {

	Page<ItemAdditional> findByNameLike(String name, Pageable pageable);
    
}