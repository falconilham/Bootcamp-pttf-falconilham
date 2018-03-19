package com.ptff.qsystem.data;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	Set<Customer> findByStatusAndSalesperson(CustomerStatus customerStatus, User salesPerson);
	Set<Customer> findByStatus(CustomerStatus awaitingApproval);
}