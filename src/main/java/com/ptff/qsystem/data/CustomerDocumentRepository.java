package com.ptff.qsystem.data;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerDocumentRepository extends JpaRepository<CustomerDocument, Long> {

	Set<CustomerDocument> findByCustomer(Customer customer);
    
}