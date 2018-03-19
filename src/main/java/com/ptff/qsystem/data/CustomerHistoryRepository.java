package com.ptff.qsystem.data;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerHistoryRepository extends JpaRepository<CustomerHistory, Long> {
	Set<CustomerHistory> findByCustomerOrderByCreateTimeDesc(Customer customer);
    
}