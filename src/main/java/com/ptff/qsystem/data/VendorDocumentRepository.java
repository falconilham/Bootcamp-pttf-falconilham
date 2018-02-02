package com.ptff.qsystem.data;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorDocumentRepository extends JpaRepository<VendorDocument, Long> {

	Set<VendorDocument> findByVendor(Vendor vendor);
    
}