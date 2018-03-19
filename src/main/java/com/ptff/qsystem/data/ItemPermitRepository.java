package com.ptff.qsystem.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPermitRepository extends JpaRepository<ItemPermit, Long> {

	Page<ItemPermit> findByNameLike(String name, Pageable pageable);
    
}