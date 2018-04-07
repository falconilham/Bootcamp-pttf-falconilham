package com.ptff.qsystem.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationRepository extends JpaRepository<Quotation, Long> {

	Long countByReference(String reference);

	List<Quotation> findAllByCustomer(Customer customer);
    
}