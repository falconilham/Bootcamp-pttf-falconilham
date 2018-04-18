package com.ptff.qsystem.data;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	Set<Customer> findByStatusAndSalespersons(CustomerStatus customerStatus, User salesPerson);
	Set<Customer> findByStatus(CustomerStatus customerStatus);
	
	@Query("select distinct c from Customer c left join c.salespersons sp where name like %:name% and (sp.fullName like %:salesperson%  or sp is null)"
			+ "and c.customerSince >= :startDate and c.customerSince <= :endDate")
	Page<Customer> findBySearchCriteria(@Param("name")String name, 
										@Param("startDate") LocalDate startDate,
										@Param("endDate") LocalDate endDate,
										@Param("salesperson") String salesperson, Pageable pageable);
	
	@Query("select distinct c from Customer c left join c.salespersons sp where name like %:name% and (sp.fullName like %:salesperson%  or sp is null) "
			+ "and c.customerSince >= :startDate and c.customerSince <= :endDate and c.status = :status")
	Page<Customer> findBySearchCriteria(@Param("name")String name, 
										@Param("startDate") LocalDate startDate,
										@Param("endDate") LocalDate endDate,
										@Param("salesperson") String salesperson,
										@Param("status") CustomerStatus status, Pageable pageable);
}