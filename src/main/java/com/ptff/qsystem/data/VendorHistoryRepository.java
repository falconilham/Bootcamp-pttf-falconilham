package com.ptff.qsystem.data;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorHistoryRepository extends JpaRepository<VendorHistory, Long> {
	Set<VendorHistory> findByVendorOrderByCreateTimeDesc(Vendor vendor);
    
}